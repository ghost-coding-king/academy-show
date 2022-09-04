package project.academyshow.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.ReviewRequest;
import project.academyshow.controller.response.ReviewResponse;
import project.academyshow.entity.Member;
import project.academyshow.entity.ReferenceType;
import project.academyshow.entity.Review;
import project.academyshow.repository.ReviewRepository;
import project.academyshow.security.AuthUtil;


@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;

    public Page<ReviewResponse> findAll(Pageable pageable) {
        return toResponseDtoPage(reviewRepository.findAll(pageable));
    }

    public Page<ReviewResponse> findAll(Pageable pageable, ReferenceType type, Long reviewedId) {
        return toResponseDtoPage(
                reviewRepository.findAllByTypeEqualsAndReviewedIdEquals(pageable, type, reviewedId));
    }

    public ReviewResponse create(ReviewRequest request, ReferenceType type, Long reviewedId) {
        Member member = AuthUtil.getLoggedInMember();
        return ReviewResponse.of(reviewRepository.save(request.toEntity(type, member, reviewedId)));
    }

    public ReviewResponse update(ReviewRequest request, Long id) {
        Review review = findById(id);
        AuthUtil.checkIsOwner(review.getMember());
        return ReviewResponse.of(review.update(request));
    }

    public void delete(Long id) {
        Review review = findById(id);
        AuthUtil.checkIsOwner(review.getMember());
        reviewRepository.delete(review);
    }

    /*
     * helpers
     */
    private Page<ReviewResponse> toResponseDtoPage(Page<Review> page) {
        return page.map(ReviewResponse::of);
    }

    private Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 리뷰를 찾을수 없습니다."));
    }
}
