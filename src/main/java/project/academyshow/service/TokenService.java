package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.academyshow.entity.ProviderType;
import project.academyshow.entity.RefreshToken;
import project.academyshow.repository.RefreshTokenRepository;
import project.academyshow.security.config.JwtConfig;
import project.academyshow.security.token.Token;
import project.academyshow.security.token.TokenProvider;
import project.academyshow.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;

    public void updateRefreshToken(
            HttpServletRequest request, HttpServletResponse response, String username, ProviderType providerType
    ) {
        Token newRefreshToken = tokenProvider.generateRefreshToken(username, providerType);
        Optional<RefreshToken> oldRefreshToken =
                refreshTokenRepository.findByUsernameAndProviderType(username, providerType);

        if (oldRefreshToken.isPresent()) {
            oldRefreshToken.get().setToken(newRefreshToken.getToken());
        }
        else {
            refreshTokenRepository.save(
                    RefreshToken.builder()
                            .username(username)
                            .token(newRefreshToken.getToken())
                            .providerType(providerType)
                            .build());
        }

        /* Refresh Token 쿠키에 등록 */
        int cookieMaxAge = (int) (jwtConfig.getRefreshTokenValidityInSeconds() / 60);
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, newRefreshToken.getToken(), cookieMaxAge);
    }
}
