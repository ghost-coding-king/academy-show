package project.academyshow.security.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import project.academyshow.entity.ProviderType;
import project.academyshow.security.config.JwtConfig;
import project.academyshow.security.entity.CustomUserDetails;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthTokenProvider implements TokenProvider {


    private final JwtConfig jwtConfig;
    private final Key key;
    private final Key refreshTokenKey;

    public AuthTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
        this.refreshTokenKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshTokenSecret().getBytes());
    }

    /** Authentication 정보로 Access Token 생성 */
    public Token generateToken(Authentication authentication) {
        return new AuthToken(generateTokenString(authentication), key);
    }

    /** Refresh Token 생성 */
    public Token generateRefreshToken(String username, ProviderType providerType) {
        return new AuthToken(generateRefreshTokenString(username, providerType), refreshTokenKey);
    }

    /** Jwt string 생성 */
    private String generateTokenString(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date expiryDate = new Date(now + jwtConfig.getTokenValidityInSeconds() * 1000);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(PROVIDER_TYPE, ((CustomUserDetails) authentication.getPrincipal()).getProviderType())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiryDate)
                .compact();
    }

    /** Refresh Token string 생성 */
    private String generateRefreshTokenString(String subject, ProviderType providerType) {
        long now = new Date().getTime();
        Date expiryDate = new Date(now + jwtConfig.getRefreshTokenValidityInSeconds() * 1000);

        return Jwts.builder()
                .setSubject(subject)
                .claim(PROVIDER_TYPE, providerType)
                .signWith(refreshTokenKey, SignatureAlgorithm.HS512)
                .setExpiration(expiryDate)
                .compact();
    }

    /** token string 을 AuthToken 객체로 변환 */
    public Token convertToAuthToken(String token) {
        return new AuthToken(token, this.key);
    }

    public Token convertToRefreshAuthToken(String token) {
        return new AuthToken(token, this.refreshTokenKey);
    }
}
