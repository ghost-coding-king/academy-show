package project.academyshow.controller.request;

import lombok.Data;
import project.academyshow.entity.Academy;
import project.academyshow.entity.Member;
import project.academyshow.entity.Subject;

import java.util.List;
import java.util.stream.Collectors;

/** 학원 회원가입 - 학원 정보 */
@Data
public class AcademyInfo {

    private String academyName;
    private List<String> educations;
    private List<Subject> subjects;
    private boolean shuttle;
    private String academyPostcode;
    private String academyRoadAddress;
    private String academyJibunAddress;
    private String academySubAddress;
    private boolean academySelectRoadAddress;
    private String introduce;
    private String registrationFile;
    private String profile;

    public Academy toEntity(Member savedMember) {
        String educations = String.join(",", this.educations);

        String subjects = this.getSubjects().stream()
                .map(Subject::getName)
                .collect(Collectors.joining(","));

        return Academy.builder()
                .member(savedMember)
                .businessRegistration(registrationFile)
                .postcode(academyPostcode)
                .roadAddress(academyRoadAddress)
                .jibunAddress(academyJibunAddress)
                .subAddress(academySubAddress)
                .selectRoadAddress(academySelectRoadAddress)
                .name(academyName)
                .shuttle(shuttle)
                .introduce(introduce)
                .educations(educations)
                .subjects(subjects)
                .profile(profile)
                .build();
    }
}
