package project.academyshow.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.entity.Member;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.service.MemberService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /** 마이페이지 - 내 정보 조회 데이터 */
    @GetMapping("/member")
    public ApiResponse<?> member(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = memberService.findByUsername(user.getUsername());
        return ApiResponse.success(new MyInfo(member));
    }

    @Data
    private static class MyInfo {
        private String username;
        private String name;
        private LocalDate birth;
        private String phone;
        private String postcode;
        private String roadAddress;
        private String jibunAddress;
        private String subAddress;
        private boolean isRoadAddress;

        public MyInfo(Member member) {
            username = member.getUsername();
            name = member.getName();
            birth = member.getBirth();
            phone = member.getPhone();
            postcode = member.getPostcode();
            roadAddress = member.getRoadAddress();
            jibunAddress = member.getJibunAddress();
            subAddress = member.getSubAddress();
            isRoadAddress = member.isSelectRoadAddress();
        }
    }
}
