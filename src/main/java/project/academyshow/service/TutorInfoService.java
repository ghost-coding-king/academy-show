package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.controller.response.ReviewStatistics;
import project.academyshow.entity.ReferenceType;
import project.academyshow.entity.SearchType;
import project.academyshow.entity.TutorInfo;
import project.academyshow.repository.ReviewRepository;
import project.academyshow.repository.TutorInfoRepository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TutorInfoService {
    private final TutorInfoRepository tutorInfoRepository;
    private final ReviewRepository reviewRepository;

    public Page<TutorInfo> search(SearchRequest searchRequest, Pageable pageable) {
        if (searchRequest.getSearchType() == SearchType.FILTER)
            return tutorInfoRepository.findAll(searchRequest, pageable);
        else
            return tutorInfoRepository.findByQuery(searchRequest.getQ(), pageable);
    }

    public Optional<TutorInfo> findById(Long id) {
        return tutorInfoRepository.findById(id);
    }

    public ReviewStatistics reviewStatistics(Long id) {
        List<Tuple> tuples = reviewRepository.countGroupByRatingForType(ReferenceType.TUTOR, id);
        return new ReviewStatistics(tuples);
    }
}
