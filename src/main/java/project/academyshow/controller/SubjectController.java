package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.repository.SubjectRepository;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectRepository subjectRepository;

    @GetMapping("/subjects")
    public ApiResponse<?> subjectList() {
        return ApiResponse.success(subjectRepository.findAll());
    }
}
