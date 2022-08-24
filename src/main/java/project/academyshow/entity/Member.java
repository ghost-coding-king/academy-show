package project.academyshow.entity;

import lombok.*;
import project.academyshow.controller.request.MyInfo;

import javax.persistence.*;
import java.time.LocalDate;

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

    /** 내 정보 수정 */
    public void updateMember(MyInfo myInfo) {
        this.phone = myInfo.getPhone();
        this.postcode = myInfo.getPostcode();
        this.roadAddress = myInfo.getRoadAddress();
        this.jibunAddress = myInfo.getJibunAddress();
        this.subAddress = myInfo.getSubAddress();
        this.selectRoadAddress = myInfo.isSelectRoadAddress();
    }
}
