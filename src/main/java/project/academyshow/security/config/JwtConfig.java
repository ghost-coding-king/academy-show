package project.academyshow.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.academyshow.security.token.JwtAuthTokenProvider;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public JwtAuthTokenProvider jwtAuthTokenProvider() {
        return new JwtAuthTokenProvider(secret);
    }
}
