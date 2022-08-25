package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.entity.Academy;
import project.academyshow.repository.AcademyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AcademyService {

    private final AcademyRepository academyRepository;

    public List<Academy> search(SearchRequest searchRequest) {
        return academyRepository.findAll();
    }
}
