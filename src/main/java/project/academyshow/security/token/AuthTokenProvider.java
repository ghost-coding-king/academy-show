package project.academyshow.security.token;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class AuthTokenProvider {

    private final Key key;
    private final long tokenValidityInMilliseconds;

    public AuthTokenProvider(@Value("${jwt.secret}") String secret,
                             @Value("${jwt.token-validity-in-seconds}") long validityInSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.tokenValidityInMilliseconds = validityInSeconds * 1000;
    }

    /** Authentication 정보로 토큰 생성 */
    public AuthToken createToken(Authentication authentication) {
        long now = new Date().getTime();
        return new AuthToken(authentication, key, new Date(now + tokenValidityInMilliseconds));
    }

    /** token string 을 JwtToken 객체로 변환 */
    public AuthToken convertToAuthToken(String token) {
        return new AuthToken(token, this.key);
    }
}
