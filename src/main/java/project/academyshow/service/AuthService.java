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

    /** ?????? ???????????? */
    public void userSignUp(UserSignUpRequest userInfo) {
        memberRepository.save(userInfo.toEntity(passwordEncoder, RoleType.ROLE_MEMBER));
    }

    /** ?????? ???????????? */
    public void academySignUp(UserSignUpRequest userInfo, AcademyInfo academyInfo) {
        /* ?????? ?????? ?????? */
        Member savedMember = memberRepository.save(userInfo.toEntity(passwordEncoder, RoleType.ROLE_ACADEMY));

        BatchLikes batchLikes = batchLikesRepository.save(new BatchLikes());

        /* ?????? ?????? */
        Academy academy = academyRepository.save(academyInfo.toEntity(savedMember, batchLikes));
    }

    /** ?????? ???????????? */
    public void tutorSignUp(UserSignUpRequest userInfo, TutorRequest tutorRequest) {
        /* ?????? ?????? ?????? */
        Member savedMember = memberRepository.save(userInfo.toEntity(passwordEncoder, RoleType.ROLE_TUTOR));

        BatchLikes batchLikes = batchLikesRepository.save(new BatchLikes());
        /* ?????? ?????? */
        TutorInfo tutorInfo = tutorInfoRepository.save(tutorRequest.toEntity(savedMember, batchLikes));
    }

    /** username ?????? ?????? */
    public boolean usernameCheck(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    /** ?????????
     * @return AuthToken */
    public Token login(HttpServletRequest request,
                           HttpServletResponse response,
                           LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            /* username, password ?????? */
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
            );

            /* AccessToken, Refresh Token ?????? */
            Token accessToken = tokenProvider.generateToken(authenticate);
            /* ????????? ???, Refresh Token ??? ????????? */
            tokenService.updateRefreshToken(request, response, username, ProviderType.LOCAL);

            return accessToken;
        } catch (Exception exception) {
            log.info("????????? ?????? - {}", exception.getLocalizedMessage());
            return null;
        }
    }

    /** Refresh Token ?????? Access Token ?????????
     * Refresh Token ??? ????????? 3??? ???????????? Refresh Token ??? ????????? */
    public Token refresh(HttpServletRequest request, HttpServletResponse response) {
        /* ????????? ?????? Access Token, Refresh Token ?????? ??? ?????? */
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

        /* Refresh Token ?????? ?????? */
        if (!StringUtils.hasText(refreshTokenString)) return null;

        /* DB ??? ????????? ????????? ?????? ?????? */
        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository
                .findByUsernameAndToken(username, refreshTokenString);
        if (oldRefreshToken.isEmpty()) return null;

        /* ????????? Refresh Token ?????? ?????? */
        Token refreshToken = tokenProvider.convertToRefreshAuthToken(refreshTokenString);
        if (!refreshToken.isValid()) return null;

        /* Refresh Token ??? ??????????????? 3??? ????????? ?????? ?????? ????????? */
        Date now = new Date();
        long validTime = refreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        if (validTime <= THREE_DAYS_IN_MILLISECONDS) {
            tokenService.updateRefreshToken(request, response, username, providerType);
        }

        /* Access Token ?????? */
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
