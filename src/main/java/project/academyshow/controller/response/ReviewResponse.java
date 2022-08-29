package project.academyshow.controller.response;

import lombok.Getter;
import project.academyshow.entity.Review;

@Getter
public class ReviewResponse extends AbstractAcademyshowResponse{
    private Long id;
    private Long reviewedId;
    private Review.TYPE type;

    private String name;

    private String comment;
    private Integer rating;

    private ReviewResponse(Review review) {
        super(review);
        id = review.getId();
        reviewedId = review.getReviewedId();
        type = review.getType();
        name = review.getMember().getName();
        comment = review.getComment();
        rating = review.getRating();
    }

    public static ReviewResponse of(Review review) {
        return new ReviewResponse(review);
    }
}
