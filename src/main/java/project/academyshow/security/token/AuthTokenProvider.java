package project.academyshow.security.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthTokenProvider {

    private final Key key;
    private final Key refreshTokenKey;
    public final long tokenValidityInMilliseconds;
    public final long refreshTokenValidityInMilliseconds;
    private static final String AUTHORITIES_KEY = "role";
    public static final String TOKEN_PREFIX = "Bearer ";

    public AuthTokenProvider(@Value("${jwt.secret}") String secret,
                             @Value("${refresh-token-secret}") String refreshTokenSecret,
                             @Value("${jwt.token-validity-in-seconds}") long validityInSeconds,
                             @Value("{refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
        this.tokenValidityInMilliseconds = validityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
    }

    /** Authentication 정보로 Access Token 생성 */
    public AuthToken generateToken(Authentication authentication) {
        return new AuthToken(generateTokenString(authentication), key);
    }

    /** Refresh Token 생성 */
    public AuthToken generateRefreshToken(String username) {
        return new AuthToken(generateRefreshTokenString(username), refreshTokenKey);
    }

    /** Jwt string 생성 */
    private String generateTokenString(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date expiryDate = new Date(now + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiryDate)
                .compact();
    }

    /** Refresh Token string 생성 */
    private String generateRefreshTokenString(String subject) {
        long now = new Date().getTime();
        Date expiryDate = new Date(now + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(subject)
                .signWith(refreshTokenKey, SignatureAlgorithm.HS512)
                .setExpiration(expiryDate)
                .compact();
    }

    /** token string 을 AuthToken 객체로 변환 */
    public AuthToken convertToAuthToken(String token) {
        return new AuthToken(token, this.key);
    }

    public AuthToken convertToRefreshAuthToken(String token) {
        return new AuthToken(token, this.refreshTokenKey);
    }
}
