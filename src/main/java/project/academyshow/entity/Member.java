package project.academyshow.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
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

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

}
