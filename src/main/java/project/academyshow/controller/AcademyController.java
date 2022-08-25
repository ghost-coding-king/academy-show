package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.service.AcademyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AcademyController {

    private final AcademyService academyService;

    @GetMapping("/academies")
    public ApiResponse<?> academies(SearchRequest searchRequest) {
        return ApiResponse.success(academyService.search(searchRequest));
    }
}
