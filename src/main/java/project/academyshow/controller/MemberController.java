package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.MyInfo;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.entity.Member;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.service.MemberService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /** 마이페이지 - 내 정보 조회 */
    @GetMapping("/member")
    public ApiResponse<?> member(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = memberService.findByUsername(user.getUsername());
        return ApiResponse.success(new MyInfo(member));
    }

    /** 마이페이지 - 내 정보 수정 */
    @PatchMapping("/member")
    public ApiResponse<?> updateMember(@AuthenticationPrincipal CustomUserDetails user,
                                       @RequestBody MyInfo myInfo) {
        memberService.updateMember(user.getUsername(), myInfo);
        return ApiResponse.success(null);
    }
}
