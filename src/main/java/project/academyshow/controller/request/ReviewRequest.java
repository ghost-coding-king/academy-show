package project.academyshow.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.academyshow.entity.Member;
import project.academyshow.entity.ReferenceType;
import project.academyshow.entity.Review;


@Getter
@NoArgsConstructor
public class ReviewRequest {
    private String comment;
    private Integer rating;
    private String reviewAge;

    public Review toEntity(ReferenceType type, Member member, Long referenceId) {
        return Review.builder()
                .type(type)
                .referenceId(referenceId)
                .comment(comment)
                .rating(rating)
                .reviewAge(reviewAge)
                .member(member)
                .build();
    }
}
