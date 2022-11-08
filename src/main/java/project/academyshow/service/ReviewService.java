package project.academyshow.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.ReviewRequest;
import project.academyshow.controller.response.ReviewResponse;
import project.academyshow.entity.Member;
import project.academyshow.entity.ProviderType;
import project.academyshow.entity.ReferenceType;
import project.academyshow.entity.Review;
import project.academyshow.repository.MemberRepository;
import project.academyshow.repository.ReviewRepository;
import project.academyshow.security.entity.CustomUserDetails;


@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final MemberService memberService;

    public Page<ReviewResponse> findAll(Pageable pageable) {
        return toResponseDtoPage(reviewRepository.findAll(pageable));
    }

    public Page<ReviewResponse> findAll(Pageable pageable, ReferenceType type, Long reviewedId) {
        return toResponseDtoPage(
                reviewRepository.findAllByTypeEqualsAndReviewedIdEquals(pageable, type, reviewedId));
    }

    public ReviewResponse create(ReviewRequest request,
                                 ReferenceType type,
                                 Long referenceId,
                                 CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails.getUsername(), userDetails.getProviderType());

        return ReviewResponse.of(reviewRepository.save(request.toEntity(type, member, referenceId)));
    }

    public ReviewResponse update(ReviewRequest request, Long id, CustomUserDetails userDetails) {
        Review review = findById(id);

        Member user = findByUsernameAndProviderType(userDetails.getUsername(), userDetails.getProviderType());

        if(user != review.getMember())
            throw new RuntimeException("안돼 돌아가");

        return ReviewResponse.of(review.update(request));
    }

    public void delete(Long id, CustomUserDetails userDetails) {
        Review review = findById(id);

        Member user = findByUsernameAndProviderType(userDetails.getUsername(), userDetails.getProviderType());

        if(user != review.getMember())
            throw new RuntimeException("안돼 돌아가");

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

    private Member findByUsernameAndProviderType(String username, ProviderType providerType) {
        return memberRepository.findByUsernameAndProviderType(username, providerType)
                .orElseThrow(() -> new RuntimeException("유저를 찾을수 없습니다."));
    }
}
