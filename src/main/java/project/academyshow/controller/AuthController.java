package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.LoginRequest;
import project.academyshow.controller.request.UserSignUpRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.security.token.AuthToken;
import project.academyshow.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /** 회원가입 */
    @PostMapping("/sign-up/user")
    public ResponseEntity<?> userSignUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        authService.userSignUp(userSignUpRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/sign-up/username-check")
    public ApiResponse<?> usernameCheck(String username) {
        return ApiResponse.success(authService.usernameCheck(username));
    }

    /** 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestBody LoginRequest loginRequest) {
        AuthToken accessToken = authService.login(request, response, loginRequest);

        return accessToken == null ? ResponseEntity.ok(ApiResponse.authenticateFailed()) :
                ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                        .body(ApiResponse.success(null));
    }

    /** Access Token 재발급 */
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
                                          HttpServletResponse response) {
        AuthToken accessToken = authService.refresh(request, response);
        return accessToken == null ? ResponseEntity.ok(ApiResponse.authenticateFailed()) :
                ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                        .body(ApiResponse.success(null));
    }
}
