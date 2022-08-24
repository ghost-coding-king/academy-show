package project.academyshow.controller.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import project.academyshow.entity.Member;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MyInfo {

    private String username;
    private String name;
    private String profile;
    private LocalDate birth;
    private String phone;
    private String postcode;
    private String roadAddress;
    private String jibunAddress;
    private String subAddress;
    private boolean selectRoadAddress;

    public MyInfo(Member member) {
        username = member.getUsername();
        name = member.getName();
        birth = member.getBirth();
        phone = member.getPhone();
        postcode = member.getPostcode();
        roadAddress = member.getRoadAddress();
        jibunAddress = member.getJibunAddress();
        subAddress = member.getSubAddress();
        selectRoadAddress = member.isSelectRoadAddress();
        profile = member.getProfile();
    }

}
