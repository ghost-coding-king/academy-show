package project.academyshow.entity;

import lombok.*;
import project.academyshow.controller.request.ReviewRequest;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends AbstractTimestampEntity {
    public enum TYPE {
        ACADEMY, TUTOR
    }

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private TYPE type;

    @Column
    private Long reviewedId;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String comment;

    @Column
    private Integer rating;

    public Review update(ReviewRequest request) {
        this.comment = request.getComment();
        this.rating = request.getRating();
        return this;
    }
}
