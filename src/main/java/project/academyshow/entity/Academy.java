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
public class Academy {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Lob
    private String introduce;

    private String postcode;
    private String roadAddress;
    private String jibunAddress;
    private String subAddress;
    private boolean selectRoadAddress;

    private String subjects;

    private String educations;

    private boolean shuttle;

    private String businessRegistration;
    private String profile;

    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_likes_id")
    private BatchLikes batchLikes;

    public BatchLikes getBatchLikes() {
        return batchLikes;
    }
}
