package project.academyshow.controller.request;

import lombok.Data;
import project.academyshow.entity.Education;
import project.academyshow.entity.Subject;

import java.util.List;

/** 학원 회원가입 추가 정보 */
@Data
public class AcademySignUpRequest {

    private String academyName;
    private List<Education> educations;
    private List<Subject> subjects;
    private boolean shuttle;
    private String academyAddress;
    private String introduce;
    private String registrationFile;
}
