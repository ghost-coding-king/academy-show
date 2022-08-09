package project.academyshow.entity;

import javax.persistence.*;

@Entity
public class AcademyEducation {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Education education;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academy_id")
    private Academy academy;
}
