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
    public ApiResponse<?> findById(@PathVariable Long id) {
        return ApiResponse.success(PostResponse.of(postService.findById(id)));
    }

    @GetMapping("/posts")
    public ApiResponse<?> findAll(PostCategory category, Pageable pageable) {
        return ApiResponse.success(
                postService.findAllByCategory(category, pageable)
                .map(PostResponse::of));
    }
}
