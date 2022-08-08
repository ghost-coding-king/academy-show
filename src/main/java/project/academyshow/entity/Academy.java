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

    @ElementCollection
    @CollectionTable(
            name = "academy_subject",
            joinColumns = @JoinColumn(name = "id")
    )
    @Column(name = "subject")
    private List<String> subjects;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(
            name = "academy_education",
            joinColumns = @JoinColumn(name = "id")
    )
    @Column(name = "education")
    private List<Education> educations;

    private boolean shuttle;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_file_id")
    private FileInfo businessRegistration;
}
