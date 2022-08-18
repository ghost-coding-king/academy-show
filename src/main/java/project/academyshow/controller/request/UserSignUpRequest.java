package project.academyshow.controller.request;

import lombok.Data;

import java.time.LocalDate;

/** 일반회원 회원가입 요청 폼 */
@Data
public class UserSignUpRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private LocalDate birth;
    private String address;
}
