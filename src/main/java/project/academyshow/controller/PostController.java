package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.PostRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.controller.response.PostResponse;
import project.academyshow.entity.Post;
import project.academyshow.entity.PostCategory;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.service.PostService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ApiResponse<?> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestBody PostRequest postRequest) {
        Post newsPost = postService.save(postRequest, userDetails);
        return ApiResponse.success(newsPost.getId());
    }

    @GetMapping("/posts/{id}")
    public ApiResponse<?> findById(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @PathVariable Long id) {
        return ApiResponse.success(postService.findById(id, userDetails));
    }

    @GetMapping("/posts")
    public ApiResponse<?> findAll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  Pageable pageable) {
        return ApiResponse.success(postService.findAll(pageable));
    }
}
