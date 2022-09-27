package project.academyshow.controller;

import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.AcademySignUpRequest;
import project.academyshow.controller.request.LoginRequest;
import project.academyshow.controller.request.TutorSignUpRequest;
import project.academyshow.controller.request.UserSignUpRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.entity.Member;
import project.academyshow.entity.RoleType;
import project.academyshow.repository.MemberRepository;
import project.academyshow.security.token.AuthToken;
import project.academyshow.security.token.AuthTokenProvider;
import project.academyshow.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberRepository memberRepository;

    /** 일반 회원가입 */
    @PostMapping("/sign-up/user")
    public ApiResponse<?> userSignUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        authService.userSignUp(userSignUpRequest);
        return ApiResponse.success(null);
    }

    /** 학원 회원가입 */
    @PostMapping("/sign-up/academy")
    public ApiResponse<?> academySignUp(@RequestBody AcademySignUpRequest academySignUpRequest) {
        authService.academySignUp(academySignUpRequest.getUserInfo(), academySignUpRequest.getAcademyInfo());
        return ApiResponse.success(null);
    }

    /** 괴외 회원가입 */
    @PostMapping("/sign-up/tutor")
    public ApiResponse<?> tutorSignUp(@RequestBody TutorSignUpRequest tutorSignUpRequest) {
        authService.tutorSignUp(tutorSignUpRequest.getUserInfo(), tutorSignUpRequest.getTutorInfo());
        return ApiResponse.success(null);
    }

    /** username 중복 확인 */
    @PostMapping("/sign-up/username-check")
    public ApiResponse<?> usernameCheck(@RequestBody SimpleUsernameRequest request) {
        return ApiResponse.success(authService.usernameCheck(request.getUsername()));
    }

    @Data
    private static class SimpleUsernameRequest {
        private String username;
    }

    /** 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestBody LoginRequest loginRequest) {
        AuthToken accessToken = authService.login(request, response, loginRequest);
        if (accessToken == null)
            return ResponseEntity.ok(ApiResponse.AUTHENTICATE_FAILED_RESPONSE);
        else {
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                    .body(ApiResponse.success(loginInfo(accessToken)));
        }
    }

    /** Access Token 재발급 */
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
                                          HttpServletResponse response) {
        AuthToken accessToken = authService.refresh(request, response);
        if (accessToken == null)
            return ResponseEntity.ok(ApiResponse.AUTHENTICATE_FAILED_RESPONSE);
        else {
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                    .body(ApiResponse.success(loginInfo(accessToken)));
        }
    }

    @Data
    private static class LoginInfo {
        private String name;
        private String profile;
        private RoleType role;
        private Long myAcademyId;
        private Long myTutorId;
    }

    /** Access Token 발급 후 username, role 정보 */
    private LoginInfo loginInfo(AuthToken accessToken) {
        LoginInfo loginInfo = new LoginInfo();
        Claims claims = accessToken.getTokenClaims();
        String username = claims.getSubject();
        Optional<Member> member = memberRepository.findByUsername(username);
        member.orElseThrow(() -> new UsernameNotFoundException("Username not found."));

        loginInfo.setName(member.get().getName());
        loginInfo.setProfile(member.get().getProfile());
        loginInfo.setRole(RoleType.valueOf(claims.get(AuthTokenProvider.AUTHORITIES_KEY).toString()));
        if (loginInfo.getRole() == RoleType.ROLE_ACADEMY)
            loginInfo.setMyAcademyId(member.get().getAcademy().getId());
        else if (loginInfo.getRole() == RoleType.ROLE_TUTOR)
            loginInfo.setMyTutorId(member.get().getTutorInfo().getId());
        return loginInfo;
    }
}
