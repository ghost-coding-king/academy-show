package project.academyshow.security.token;

import org.springframework.security.core.Authentication;


public interface TokenProvider {

    String AUTHORITIES_KEY = "role";
    String TOKEN_PREFIX = "Bearer ";

    Token generateToken(Authentication authentication);
    Token generateRefreshToken(String username);
    Token convertToAuthToken(String token);
    Token convertToRefreshAuthToken(String token);

}
