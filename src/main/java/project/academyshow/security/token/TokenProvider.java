package project.academyshow.security.token;

import org.springframework.security.core.Authentication;
import project.academyshow.entity.ProviderType;


public interface TokenProvider {

    String AUTHORITIES_KEY = "role";
    String PROVIDER_TYPE = "provider_type";
    String TOKEN_PREFIX = "Bearer ";

    Token generateToken(Authentication authentication);
    Token generateRefreshToken(String username, ProviderType providerType);
    Token convertToAuthToken(String token);
    Token convertToRefreshAuthToken(String token);

}
