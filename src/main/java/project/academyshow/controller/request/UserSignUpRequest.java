package project.academyshow.controller.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.academyshow.entity.Member;
import project.academyshow.entity.ProviderType;
import project.academyshow.entity.RoleType;

import java.time.LocalDate;

/** 일반회원 회원가입 요청 폼 */
@Data
@RequiredArgsConstructor
public class UserSignUpRequest {

    private String username;
    private String password;
    private String name;
    private String phone;
    private LocalDate birth;
    private String address;

    public Member toEntity(PasswordEncoder passwordEncoder, RoleType role) {
        return Member.builder()
                .username(this.getUsername())
                .password(passwordEncoder.encode(password))
                .name(name)
                .phone(phone)
                .birth(birth)
                .address(address)
                .role(role)
                .providerType(ProviderType.LOCAL)
                .build();
    }
}
