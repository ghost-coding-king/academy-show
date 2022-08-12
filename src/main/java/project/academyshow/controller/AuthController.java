package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.academyshow.controller.request.LoginRequest;
import project.academyshow.controller.request.SignUpRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.security.token.AuthToken;
import project.academyshow.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** 회원가입 */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /** 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        AuthToken authToken = authService.login(loginRequest);

        return authToken == null ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() :
                ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authToken.getToken())
                .body(ApiResponse.success(null));
    }
}
