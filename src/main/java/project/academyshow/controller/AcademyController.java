package project.academyshow.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.entity.Academy;
import project.academyshow.service.AcademyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AcademyController {

    private final AcademyService academyService;

    /** 학원 검색 (Pageable: page(페이지), size(페이지 당 개수), sort(정렬 기준 필드명과 정렬방법) */
    @GetMapping("/academies")
    public ApiResponse<?> academies(SearchRequest searchRequest, Pageable pageable) {
        Page<AcademySearchResponse> responseData = academyService.search(searchRequest, pageable)
                .map(AcademySearchResponse::new);
        return ApiResponse.success(responseData);
    }

    @Data
    private static class AcademySearchResponse {
        private Long id;
        private String name;
        private String profile;
        private String introduce;
        private String roadAddress;
        private String subAddress;

        private AcademySearchResponse(Academy academy) {
            this.id = academy.getId();
            this.name = academy.getName();
            this.profile = academy.getProfile();
            this.introduce = academy.getIntroduce();
            this.roadAddress = academy.getRoadAddress();
            this.subAddress = academy.getSubAddress();
        }
    }
}
