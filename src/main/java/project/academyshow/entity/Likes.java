package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * referenceId를 사용하면 정합성 체크를 데이터베이스가 아닌 어플리케이션에서 해줘야한다.
 * 관련 자료가 많이 없다.!
 * 그래서 JPA 관련도서에 소개된 단일 테이블 전략으로 새로 설계해봤다.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Likes extends AbstractTimestampEntity {
    @Id
    @GeneratedValue
    private Long id;

//    @Enumerated(EnumType.STRING)
//    private LikesType type;

    /** component likes are added */
//    @Column
//    private Long referenceId;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne
    @JoinColumn(name = "academy_id")
    private Academy academy;

    @OneToOne
    @JoinColumn(name = "tutor_info_id")
    private TutorInfo tutorInfo;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
