package project.academyshow.entity;

import javax.persistence.*;

@Entity
public class TutorEducation {
    
    @Id @GeneratedValue
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private Education education;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_info_id")
    private TutorInfo tutorInfo;
}
