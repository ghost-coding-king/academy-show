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

    /** 좋아요를 클릭하거나 클릭 해제 한다 */
    @PostMapping("/up")
    public ApiResponse<?> createOrDestroy(@AuthenticationPrincipal CustomUserDetails user,
                                          @RequestBody LikesRequest request) {
        likeService.createOrDestroy(request, user);
        return ApiResponse.SUCCESS_NO_DATA_RESPONSE;
    }

    /** 좋아요 정보 가져오기 */
    @GetMapping("/up")
    public ApiResponse<?> up(@AuthenticationPrincipal CustomUserDetails user,
                             LikesRequest request) {
        ReferenceLikesStatistics likeInfo =
                likeService.getLikeInfoByReference(request.getType(), request.getReferenceId(), user);
        return ApiResponse.success(likeInfo);
    }
}
