package project.academyshow.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Academy {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Lob
    private String introduce;

    private String address;

    @OneToMany(mappedBy = "academy")
    private List<AcademySubject> subjects;

    @OneToMany(mappedBy = "academy")
    private List<AcademyEducation> educations;

    private boolean shuttle;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_file_id")
    private FileInfo businessRegistration;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
