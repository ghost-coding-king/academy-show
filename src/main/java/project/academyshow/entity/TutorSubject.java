package project.academyshow.entity;

import javax.persistence.*;

@Entity
public class TutorSubject {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_info_id")
    private TutorInfo tutorInfo;
}
