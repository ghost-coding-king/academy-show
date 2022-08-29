package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.PostRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.controller.response.PostResponse;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.service.PostService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ApiResponse<?> create(@AuthenticationPrincipal CustomUserDetails user,
                                @RequestBody PostRequest postRequest) {
        postService.save(postRequest, user.getMember());
        return ApiResponse.success(null);
    }

    @GetMapping("/posts/{id}")
    public ApiResponse<?> findById(@PathVariable Long id) {
        return ApiResponse.success(PostResponse.of(postService.findById(id)));
    }
}
