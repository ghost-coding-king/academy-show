package project.academyshow.security.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import project.academyshow.entity.Member;
import project.academyshow.repository.MemberRepository;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.security.token.Token;
import project.academyshow.security.token.TokenProvider;
import project.academyshow.util.HeaderUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = HeaderUtil.resolveToken(request);
        if (!StringUtils.hasText(token)) {
            log.debug("토큰 정보 없음");
        }
        else {
            /* token string -> AuthToken object */
            Token authToken = tokenProvider.convertToAuthToken(token);

            if (authToken.isValid()) {
                Claims claims = authToken.getTokenClaims();
                Optional<Member> member = memberRepository.findByUsername(claims.getSubject());
                member.orElseThrow(() -> new UsernameNotFoundException("Username not found."));

                List<SimpleGrantedAuthority> authorities =
                Arrays.stream(claims.get(TokenProvider.AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

                /* CustomUserDetails 생성하여 Authentication 등록 */
                CustomUserDetails userDetails = CustomUserDetails.builder().member(member.get()).build();
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, "", authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context => 인증 정보 저장 완료: {}", authentication.getName());
            }
        }

        filterChain.doFilter(request, response);
    }
}
