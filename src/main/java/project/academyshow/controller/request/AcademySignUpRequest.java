package project.academyshow.controller.request;

import lombok.Data;

/** 학원 회원가입 추가 정보 */
@Data
public class AcademySignUpRequest {

    private UserSignUpRequest userInfo;
    private AcademyInfo academyInfo;
}
