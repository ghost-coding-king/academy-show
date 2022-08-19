package project.academyshow.controller.request;

import lombok.Data;

@Data
public class TutorSignUpRequest {
    private UserSignUpRequest userInfo;
    private TutorRequest tutorInfo;
}
