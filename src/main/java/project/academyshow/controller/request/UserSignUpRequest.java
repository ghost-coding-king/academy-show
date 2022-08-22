package project.academyshow.controller.request;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.academyshow.entity.Member;
import project.academyshow.entity.ProviderType;
import project.academyshow.entity.RoleType;

import java.time.LocalDate;

/** 일반회원 회원가입 요청 폼 */
@Data
public class UserSignUpRequest {

    private String username;
    private String password;
    private String name;
    private String phone;
    private LocalDate birth;
    private String postcode;
    private String roadAddress;
    private String jibunAddress;
    private String subAddress;
    private boolean selectRoadAddress;

    public Member toEntity(PasswordEncoder passwordEncoder, RoleType role) {
        return Member.builder()
                .username(this.getUsername())
                .password(passwordEncoder.encode(password))
                .name(name)
                .phone(phone)
                .birth(birth)
                .postcode(postcode)
                .roadAddress(roadAddress)
                .jibunAddress(jibunAddress)
                .subAddress(subAddress)
                .selectRoadAddress(selectRoadAddress)
                .role(role)
                .providerType(ProviderType.LOCAL)
                .build();
    }
}
