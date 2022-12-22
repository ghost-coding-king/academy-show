package project.academyshow.entity;

import lombok.*;
import org.springframework.util.StringUtils;
import project.academyshow.controller.request.MyInfo;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private LocalDate birth;
    private String postcode;
    private String roadAddress;
    private String jibunAddress;
    private String subAddress;
    private boolean selectRoadAddress;
    private String profile;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Academy> academy;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TutorInfo> tutorInfo;

    /** 내 정보 수정 */
    public void updateMember(MyInfo myInfo) {
        if (!StringUtils.hasText(myInfo.getProfile())) myInfo.setProfile(null);

        this.phone = myInfo.getPhone();
        this.postcode = myInfo.getPostcode();
        this.roadAddress = myInfo.getRoadAddress();
        this.jibunAddress = myInfo.getJibunAddress();
        this.subAddress = myInfo.getSubAddress();
        this.selectRoadAddress = myInfo.isSelectRoadAddress();
        this.profile = myInfo.getProfile();
    }

    public Academy getAcademy() {
        return academy.get(0);
    }

    public TutorInfo getTutorInfo() {
        return tutorInfo.get(0);
    }
}
