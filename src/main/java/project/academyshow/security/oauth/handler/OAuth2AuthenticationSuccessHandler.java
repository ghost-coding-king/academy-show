package project.academyshow.security.oauth.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import project.academyshow.config.AppProperties;
import project.academyshow.entity.ProviderType;
import project.academyshow.security.oauth.entity.OAuth2UserInfo;
import project.academyshow.security.oauth.entity.OAuth2UserInfoFactory;
import project.academyshow.security.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import project.academyshow.security.token.Token;
import project.academyshow.security.token.TokenProvider;
import project.academyshow.service.TokenService;
import project.academyshow.util.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REDIRECT_URI;


@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException {

        if (response.isCommitted()) {
            return;
        }

        /* access token 생성 */
        Token accessToken = jwtProcess(request, response, authentication);

        /* frontend redirect url 로 이동, 토큰 파라미터로 전달 */
        String targetUrl = determineTargetUrl(request, response, authentication);
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken.getToken())
                .encode(StandardCharsets.UTF_8)
                .build().toUriString();

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("허용되지 않은 Redirect URI.");
        }

        return redirectUri.orElse(getDefaultTargetUrl());
    }

    /* access token, refresh token 에 관한 처리 */
    private Token jwtProcess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;

        ProviderType providerType = ProviderType.valueOf(oauth2Token.getAuthorizedClientRegistrationId().toUpperCase());
        OidcUser user = ((OidcUser) authentication.getPrincipal());
        OAuth2UserInfo oAuth2User = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());

        Token accessToken = tokenProvider.generateToken(authentication);

        /* Refresh Token 처리 */
        tokenService.updateRefreshToken(request, response, oAuth2User.getId(), providerType);

        return accessToken;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOAuth2AuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // host, port 검사
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
