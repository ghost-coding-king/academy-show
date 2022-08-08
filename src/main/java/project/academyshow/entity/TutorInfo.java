package project.academyshow.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class TutorInfo {

    @Id @GeneratedValue
    private Long id;

    private String scholarship;

    @ElementCollection
    @CollectionTable(
            name = "tutor_subject",
            joinColumns = @JoinColumn(name = "id")
    )
    @Column(name = "subject")
    private List<String> subjects;
}
