package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.controller.response.ReviewStatistics;
import project.academyshow.entity.Academy;
import project.academyshow.entity.Review;
import project.academyshow.repository.AcademyRepository;
import project.academyshow.repository.PostRepository;
import project.academyshow.repository.ReviewRepository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AcademyService {

    private final AcademyRepository academyRepository;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;

    public Page<Academy> search(SearchRequest searchRequest, Pageable pageable) {
        return academyRepository.findAll(searchRequest, pageable);
    }

    public Optional<Academy> findById(Long id) {
        return academyRepository.findById(id);
    }

    public ReviewStatistics reviewStatistics(Long id) {
        List<Tuple> tuples = reviewRepository.countGroupByRatingForType(Review.TYPE.ACADEMY, id);
        return new ReviewStatistics(tuples);
    }

    public void findPostList(Long id, Pageable pageable) {

    }

    public void findPost(Long academyId, Long postId) {

    }
}