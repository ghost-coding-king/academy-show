package project.academyshow.util;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import project.academyshow.security.token.TokenProvider;

import javax.servlet.http.HttpServletRequest;

public class HeaderUtil {

    /** Http header 에서 Jwt string 파싱 */
    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TokenProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(TokenProvider.TOKEN_PREFIX.length());
        }

        return null;
    }
}
