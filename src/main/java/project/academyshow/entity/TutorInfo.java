package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorInfo {

    @Id @GeneratedValue
    private Long id;

    // 대학
    private String scholarship;

    private String certification;

    private String subjects;

    private String educations;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
