package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TutorInfo {

    @Id @GeneratedValue
    private Long id;

    private String title;

    // 대학
    private String scholarship;

    private String certification;

    private String subjects;

    private String educations;

    private String introduce;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 검색 노출 여부
    private Boolean isExposed;

    // 대략적인 주소 정보 (강원도 강릉시)
    private String area;
}
