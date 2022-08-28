package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.academyshow.controller.request.PostSaveRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.service.PostService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ApiResponse<?> posts(@AuthenticationPrincipal CustomUserDetails user,
                                @RequestBody PostSaveRequest postSaveRequest) {
        postService.save(postSaveRequest, user.getMember());
        return ApiResponse.success(null);
    }
}
