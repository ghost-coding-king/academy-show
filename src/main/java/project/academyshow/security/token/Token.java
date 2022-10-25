package project.academyshow.security.token;

import io.jsonwebtoken.Claims;

public interface Token {

    boolean isValid();
    Claims getTokenClaims();
    String getToken();
}
