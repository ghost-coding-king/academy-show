package project.academyshow.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.academyshow.security.filter.AuthTokenFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /** CORS 관련 설정 정보 */
    private final CorsProperties corsProperties;
    /** Jwt 인증 필터 */
    private final AuthTokenFilter authTokenFilter;

    /** Spring Security Filter Chain 관련 설정 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                    .cors()
                .and()
                    /* session 사용하지 않음 (STATELESS) */
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .csrf().disable()
                    .exceptionHandling()
                    /* 유효한 자격증명을 제공하지 않는 경우 */
                    .authenticationEntryPoint(((request, response, authException) -> {
                        log.debug("Unauthorized responding");
                        response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                authException.getLocalizedMessage()
                        );
                    }))
                    /* 권한이 부족한 경우 */
                    .accessDeniedHandler(((request, response, accessDeniedException) -> {
                        log.debug("Forbidden responding");
                        response.sendError(
                                HttpServletResponse.SC_FORBIDDEN,
                                accessDeniedException.getLocalizedMessage()
                        );
                    }))
                .and()
                    /* URI 기반 인증/인가 설정 */
                    .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .antMatchers("/sign-up").permitAll()
                    .antMatchers("/login").permitAll()
                    .anyRequest().authenticated();

        /* jwtTokenFilter 등록 */
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** AuthenticationManager 설정 */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /** password 인코더 설정 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /** CORS 설정 */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setMaxAge(corsProperties.getMaxAge());
        corsConfig.setAllowCredentials(true);

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);

        return corsConfigSource;
    }
}
