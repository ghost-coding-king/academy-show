package project.academyshow.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
    private String refreshTokenSecret;
    private Long tokenValidityInSeconds;
    private Long refreshTokenValidityInSeconds;
}
