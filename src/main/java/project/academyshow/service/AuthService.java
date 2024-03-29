package project.academyshow.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import project.academyshow.controller.request.AcademyInfo;
import project.academyshow.controller.request.LoginRequest;
import project.academyshow.controller.request.TutorRequest;
import project.academyshow.controller.request.UserSignUpRequest;
import project.academyshow.entity.*;
import project.academyshow.repository.*;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.security.token.Token;
import project.academyshow.security.token.TokenProvider;
import project.academyshow.util.CookieUtil;
import project.academyshow.util.HeaderUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AcademyRepository academyRepository;
    private final TutorInfoRepository tutorInfoRepository;
    private final BatchLikesRepository batchLikesRepository;
    private final static long THREE_DAYS_IN_MILLISECONDS = 259200000;

    /** 일반 회원가입 */
    public void userSignUp(UserSignUpRequest userInfo) {
        memberRepository.save(userInfo.toEntity(passwordEncoder, RoleType.ROLE_MEMBER));
    }

    /** 학원 회원가입 */
    public void academySignUp(UserSignUpRequest userInfo, AcademyInfo academyInfo) {
        /* 회원 기본 정보 */
        Member savedMember = memberRepository.save(userInfo.toEntity(passwordEncoder, RoleType.ROLE_ACADEMY));

        BatchLikes batchLikes = batchLikesRepository.save(new BatchLikes());

        /* 학원 정보 */
        Academy academy = academyRepository.save(academyInfo.toEntity(savedMember, batchLikes));
    }

    /** 과외 회원가입 */
    public void tutorSignUp(UserSignUpRequest userInfo, TutorRequest tutorRequest) {
        /* 회원 기본 정보 */
        Member savedMember = memberRepository.save(userInfo.toEntity(passwordEncoder, RoleType.ROLE_TUTOR));

        BatchLikes batchLikes = batchLikesRepository.save(new BatchLikes());
        /* 과외 정보 */
        TutorInfo tutorInfo = tutorInfoRepository.save(tutorRequest.toEntity(savedMember, batchLikes));
    }

    /** username 중복 확인 */
    public boolean usernameCheck(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    /** 로그인
     * @return AuthToken */
    public Token login(HttpServletRequest request,
                           HttpServletResponse response,
                           LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            /* username, password 검증 */
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
            );

            /* AccessToken, Refresh Token 발급 */
            Token accessToken = tokenProvider.generateToken(authenticate);
            /* 로그인 시, Refresh Token 도 재발급 */
            tokenService.updateRefreshToken(request, response, username, ProviderType.LOCAL);

            return accessToken;
        } catch (Exception exception) {
            log.info("로그인 실패 - {}", exception.getLocalizedMessage());
            return null;
        }
    }

    /** Refresh Token 으로 Access Token 재발급
     * Refresh Token 의 만료가 3일 이내이면 Refresh Token 도 재발급 */
    public Token refresh(HttpServletRequest request, HttpServletResponse response) {
        /* 요청에 담긴 Access Token, Refresh Token 획득 및 검증 */
        String accessTokenString = HeaderUtil.resolveToken(request);
        Token accessToken = tokenProvider.convertToAuthToken(accessTokenString);

        Claims claims = accessToken.getTokenClaims();
        if (claims == null)
            return null;

        String username = claims.getSubject();
        ProviderType providerType = ProviderType.valueOf(claims.get(TokenProvider.PROVIDER_TYPE).toString());

        String refreshTokenString = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        /* Refresh Token 존재 여부 */
        if (!StringUtils.hasText(refreshTokenString)) return null;

        /* DB 에 등록된 정보를 통해 검증 */
        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository
                .findByUsernameAndToken(username, refreshTokenString);
        if (oldRefreshToken.isEmpty()) return null;

        /* 유효한 Refresh Token 인지 확인 */
        Token refreshToken = tokenProvider.convertToRefreshAuthToken(refreshTokenString);
        if (!refreshToken.isValid()) return null;

        /* Refresh Token 의 유효기간이 3일 이내로 남은 경우 재발급 */
        Date now = new Date();
        long validTime = refreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        if (validTime <= THREE_DAYS_IN_MILLISECONDS) {
            tokenService.updateRefreshToken(request, response, username, providerType);
        }

        /* Access Token 발급 */
        Optional<Member> nowMember = memberRepository.findByUsername(username);
        if (nowMember.isEmpty()) return null;

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(nowMember.get().getRole().toString()));

        CustomUserDetails userDetails = CustomUserDetails.builder()
                .username(claims.getSubject())
                .providerType(ProviderType.valueOf(claims.get(TokenProvider.PROVIDER_TYPE).toString()))
                .role(RoleType.valueOf(claims.get(TokenProvider.AUTHORITIES_KEY).toString()))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

        return tokenProvider.generateToken(authentication);
    }
}
