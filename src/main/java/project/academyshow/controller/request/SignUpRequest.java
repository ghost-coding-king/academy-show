package project.academyshow.controller.request;

import lombok.Data;

/** 테스트 회원가입 요청 폼 */
@Data
public class SignUpRequest {
    private String username;
    private String password;
}
