package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.entity.Academy;
import project.academyshow.repository.AcademyRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AcademyService {

    private final AcademyRepository academyRepository;

    public Page<Academy> search(SearchRequest searchRequest, Pageable pageable) {
        return academyRepository.findAll(searchRequest, pageable);
    }
}
