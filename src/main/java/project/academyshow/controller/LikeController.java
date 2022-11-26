package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.LikesRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.controller.response.ReferenceLikesStatistics;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.service.LikeService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes")
    public ApiResponse<?> create(@AuthenticationPrincipal CustomUserDetails user,
                                          @RequestBody LikesRequest request) {
        likeService.create(request, user);
        return ApiResponse.SUCCESS_NO_DATA_RESPONSE;
    }

    @PostMapping("/likes/dislike")
    public ApiResponse<?> destroy(@AuthenticationPrincipal CustomUserDetails user,
                                          @RequestBody LikesRequest request) {
        likeService.destroy(request, user);
        return ApiResponse.SUCCESS_NO_DATA_RESPONSE;
    }

    /** 좋아요 정보 가져오기 */
    @GetMapping("/likes")
    public ApiResponse<?> up(@AuthenticationPrincipal CustomUserDetails user,
                             LikesRequest request) {
        ReferenceLikesStatistics likeInfo =
                likeService.getLikeInfoByReference(request.getType(), request.getReferenceId(), user);
        return ApiResponse.success(likeInfo);
    }
}
