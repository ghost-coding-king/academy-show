package project.academyshow.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class TutorInfo {

    @Id @GeneratedValue
    private Long id;

    private String scholarship;

    @OneToMany(mappedBy = "tutorInfo")
    private List<TutorSubject> subjects;

    @OneToMany(mappedBy = "tutorInfo")
    private List<TutorEducation> educations;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
