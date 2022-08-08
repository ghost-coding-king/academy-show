package project.academyshow.entity;

import lombok.Getter;

@Getter
public enum Education {
    AGE5("유아", "5세"), AGE6("유아", "6세"), AGE7("유아", "7세"),
    AGE8("초등학생", "1학년"), AGE9("초등학생", "2학년"), AGE10("초등학생", "3학년"),
    AGE11("초등학생", "4학년"), AGE12("초등학생", "5학년"), AGE13("초등학생", "6학년"),
    AGE14("중학생", "1학년"), AGE15("중학생", "2학년"), AGE16("중학생", "3학년"),
    AGE17("고등학생", "1학년"), AGE18("고등학생", "2학년"), AGE19("고등학생", "3학년"),
    AGE20("고등학교", "졸업생");

    private final String schoolLevel;
    private final String grade;

    Education(String schoolLevel, String grade) {
        this.schoolLevel = schoolLevel;
        this.grade = grade;
    }
}
