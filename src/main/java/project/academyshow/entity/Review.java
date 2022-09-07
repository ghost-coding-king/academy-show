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
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReferenceType type;

    @Column
    private Long referenceId;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String comment;

    private String reviewAge;

    @Column
    private Integer rating;

    public Review update(ReviewRequest request) {
        this.comment = request.getComment();
        this.rating = request.getRating();
        return this;
    }
}
