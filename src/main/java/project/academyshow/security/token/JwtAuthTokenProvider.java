package project.academyshow.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import project.academyshow.security.exception.TokenValidFailedException;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthTokenProvider {

    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public JwtAuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public JwtAuthToken createAuthToken(String id, Date expiry) {
        return new JwtAuthToken(id, expiry, key);
    }

    public JwtAuthToken createAuthToken(String id, String role, Date expiry) {
        return new JwtAuthToken(id, role, expiry, key);
    }

    public JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token, key);
    }

    public Authentication getAuthentication(JwtAuthToken jwtAuthToken) {
        if (jwtAuthToken.validate()) {
            Claims claims = jwtAuthToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[] {claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            log.debug("claims subject := [{}]", claims.getSubject());

            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, jwtAuthToken, authorities);
        } else {
            throw new TokenValidFailedException();
        }
    }
}
