package project.academyshow.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class BatchLikes {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long count = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academy_id")
    private Academy academy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_info_id")
    private TutorInfo tutorInfo;

    public void resetCountForBatch() {
        this.count = 0L;
    }

    public void incrementCountForBatch() {
        this.count += 1L;
    }

    /**
     * batchlikes creator
     */
    public static BatchLikes of(Post post) {
        return BatchLikes.builder()
                .post(post)
                .build();
    }

    public static BatchLikes of(Academy academy) {
        return BatchLikes.builder()
                .academy(academy)
                .build();
    }

    public static BatchLikes of(TutorInfo tutorInfo) {
        return BatchLikes.builder()
                .tutorInfo(tutorInfo)
                .build();
    }
}
