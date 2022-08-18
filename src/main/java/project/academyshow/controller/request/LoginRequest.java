package project.academyshow.controller.request;

import lombok.Data;

/** 로그인 요청 정보 */
@Data
public class LoginRequest {

    private String username;
    private String password;
}
