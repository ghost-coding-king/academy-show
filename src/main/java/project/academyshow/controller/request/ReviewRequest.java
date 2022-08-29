package project.academyshow.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.academyshow.entity.Member;
import project.academyshow.entity.Review;


@Getter
@NoArgsConstructor
public class ReviewRequest {
    private String comment;
    private Integer rating;

    public Review toEntity(Review.TYPE type, Member member, Long reviewedId) {
        return Review.builder()
                .type(type)
                .reviewedId(reviewedId)
                .comment(comment)
                .rating(rating)
                .member(member)
                .build();
    }
}
