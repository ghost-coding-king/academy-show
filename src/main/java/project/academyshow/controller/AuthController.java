package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.academyshow.controller.dto.SignUpForm;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/members")
    public ApiResponse<?> signUp(@RequestBody SignUpForm form) {
        authService.saveMember(form);
        return ApiResponse.success(null);
    }
}
