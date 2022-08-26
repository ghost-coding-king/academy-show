package project.academyshow.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.entity.Academy;
import project.academyshow.service.AcademyService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AcademyController {

    private final AcademyService academyService;

    /** 학원 검색 (Pageable: page(페이지), size(페이지 당 개수), sort(정렬 기준 필드명과 정렬방법) */
    @GetMapping("/academies")
    public ApiResponse<?> academies(SearchRequest searchRequest, Pageable pageable) {
        Page<AcademySearchResponse> resources = academyService.search(searchRequest, pageable)
                .map(AcademySearchResponse::new);
        return ApiResponse.success(resources);
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
            id = academy.getId();
            name = academy.getName();
            profile = academy.getProfile();
            introduce = academy.getIntroduce();
            roadAddress = academy.getRoadAddress();
            subAddress = academy.getSubAddress();
        }
    }

    @GetMapping("/academy/{id}")
    public ApiResponse<?> academy(@PathVariable("id") Long id) {
        Optional<Academy> academy = academyService.findById(id);
        if (academy.isPresent())
            return ApiResponse.success(new AcademyResponse(academy.get()));
        else
            return ApiResponse.resourceNotFound();
    }

    @Data
    private static class AcademyResponse {
        private Long id;
        private String name;
        private String profile;
        private String introduce;
        private String roadAddress;
        private String jibunAddress;
        private String subAddress;
        private List<String> subjects;
        private List<String> educations;
        private boolean shuttle;

        private AcademyResponse(Academy academy) {
            id = academy.getId();
            name = academy.getName();
            profile = academy.getProfile();
            introduce = academy.getIntroduce();
            roadAddress = academy.getRoadAddress();
            jibunAddress = academy.getJibunAddress();
            subAddress = academy.getSubAddress();
            subjects = Arrays.stream(academy.getSubjects().split(",")).collect(Collectors.toList());
            educations = Arrays.stream(academy.getEducations().split(",")).collect(Collectors.toList());
            shuttle = academy.isShuttle();
        }
    }
}
