package project.academyshow.entity;

import lombok.*;
import javax.persistence.*;

/**
 * https://stackoverflow.com/questions/63656497/jpa-repository-with-single-table-inheritance-hibernate
 */
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Likes {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "batch_likes_id")
    private BatchLikes batchLikes;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "academy_id")
    private Academy academy;

    @ManyToOne
    @JoinColumn(name = "tutor_info_id")
    private TutorInfo tutorInfo;

    /**
     * likes creator
     */
    public static Likes createPostLikes(Member member, Post post) {
        return Likes.builder()
                .member(member)
                .post(post)
                .batchLikes(post.getBatchLikes())
                .build();
    }

    public static Likes createAcademyLikes(Member member, Academy academy) {
        return Likes.builder()
                .member(member)
                .academy(academy)
                .batchLikes(academy.getBatchLikes())
                .build();
    }

    public static Likes createTutorInfoLikes(Member member, TutorInfo tutorInfo) {
        return Likes.builder()
                .member(member)
                .tutorInfo(tutorInfo)
                .batchLikes(tutorInfo.getBatchLikes())
                .build();
    }
}
