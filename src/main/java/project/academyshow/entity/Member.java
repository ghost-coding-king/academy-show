package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
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
    private String address;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Enumerated
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

}
