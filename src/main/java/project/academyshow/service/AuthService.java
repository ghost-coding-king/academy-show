package project.academyshow.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import project.academyshow.controller.request.LoginRequest;
import project.academyshow.controller.request.SignUpRequest;
import project.academyshow.entity.Member;
import project.academyshow.entity.RefreshToken;
import project.academyshow.repository.MemberRepository;
import project.academyshow.repository.RefreshTokenRepository;
import project.academyshow.security.token.AuthToken;
import project.academyshow.security.token.AuthTokenProvider;
import project.academyshow.util.CookieUtil;
import project.academyshow.util.HeaderUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final static long THREE_DAYS_IN_MILLISECONDS = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    /** 회원가입 */
    public void signUp(SignUpRequest signUpRequest) {
        Member newMember = Member.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();

        memberRepository.save(newMember);
    }

    /** 로그인
     * @return AuthToken */
    public AuthToken login(HttpServletRequest request,
                           HttpServletResponse response,
                           LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                ));

            AuthToken accessToken = tokenProvider.generateToken(authenticate);
            AuthToken newRefreshToken = tokenProvider.generateRefreshToken(username);
            Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByUsername(username);

            if (oldRefreshToken.isPresent()) {
                oldRefreshToken.get().setToken(newRefreshToken.getToken());
            }
            else {
                refreshTokenRepository.save(
                        RefreshToken.builder()
                        .username(username)
                        .token(newRefreshToken.getToken()).build());
            }

            int cookieMaxAge = (int) tokenProvider.refreshTokenValidityInMilliseconds / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, newRefreshToken.getToken(), cookieMaxAge);

            return accessToken;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /** Refresh Token 으로 Access Token 재발급
     * Refresh Token 의 만료기간이 3일 이내이면 Refresh Token 도 재발급 */
    public AuthToken refresh(HttpServletRequest request, HttpServletResponse response) {
        String accessTokenString = HeaderUtil.resolveToken(request);
        AuthToken accessToken = tokenProvider.convertToAuthToken(accessTokenString);

        Claims claims = accessToken.getTokenClaims();
        String username = claims.getSubject();

        String refreshTokenString = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        if (!StringUtils.hasText(refreshTokenString)) return null;

        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository
                .findByUsernameAndToken(username, refreshTokenString);

        if (oldRefreshToken.isEmpty()) return null;

        AuthToken refreshToken = tokenProvider.convertToRefreshAuthToken(refreshTokenString);
        if (!refreshToken.isValid()) return null;

        Date now = new Date();
        long validTime = refreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        if (validTime <= THREE_DAYS_IN_MILLISECONDS) {
            AuthToken newRefreshToken = tokenProvider.generateRefreshToken(username);
            oldRefreshToken.get().setToken(newRefreshToken.getToken());

            int cookieMaxAge = (int) tokenProvider.refreshTokenValidityInMilliseconds / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, newRefreshToken.getToken(), cookieMaxAge);
        }

        List<SimpleGrantedAuthority> authorities =
                Arrays.stream(claims.get(AuthTokenProvider.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);

        return tokenProvider.generateToken(authentication);
    }
}
