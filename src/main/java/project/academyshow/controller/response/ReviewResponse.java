package project.academyshow.controller.response;

import lombok.Getter;
import project.academyshow.entity.Review;

@Getter
public class ReviewResponse extends AbstractAcademyshowResponse{
    private final Long id;
    private final Long reviewedId;
    private final Review.TYPE type;
    private final String name;
    private final String profile;
    private final String comment;
    private final Integer rating;
    private final String reviewAge;

    private ReviewResponse(Review review) {
        super(review);
        id = review.getId();
        reviewedId = review.getReviewedId();
        type = review.getType();
        name = review.getMember().getName();
        profile = review.getMember().getProfile();
        comment = review.getComment();
        rating = review.getRating();
        reviewAge = review.getReviewAge();
    }

    public static ReviewResponse of(Review review) {
        return new ReviewResponse(review);
    }
}
