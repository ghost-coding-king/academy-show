package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends AbstractTimestampEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private PostCategory category;

    private String title;

    @Lob
    private String content;

    @OneToOne
    @JoinColumn(name = "academy_id")
    private Academy academy;

    @OneToOne
    @JoinColumn(name = "tutor_info_id")
    private TutorInfo tutorInfo;

    public String profileOfAcademy() {
        return academy.getProfile();
    }

    public String profileOfMember() {
        return member.getProfile();
    }

    public String nameOfAcademy() {
        return academy.getName();
    }

    public String nameOfMember() {
        return member.getName();
    }
}
