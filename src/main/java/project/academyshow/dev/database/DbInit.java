package project.academyshow.dev.database;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.entity.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final ExampleInsert exampleInsert;

    @PostConstruct
    public void init() {
        exampleInsert.insertSubject();
        exampleInsert.insertMember();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class ExampleInsert {

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        public void insertSubject() {
            String[] subjects = {
                    "국어", "영어", "수학", "과학", "사회",
                    "독서/토론/논술", "경시대회", "입시컨설팅", "SW교육/코딩교육",
                    "제2외국어", "유학/SAT/AP/토플", "음악/예술", "미술", "체육",
                    "취업/자격증", "기타"
            };

            for (String subject : subjects)
                em.persist(new Subject(subject));
        }

        public void insertMember() {
            Member member = Member.builder()
                    .username("test")
                    .password(passwordEncoder.encode("test"))
                    .name("유리")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .isRoadAddress(false)
                    .role(RoleType.ROLE_MEMBER)
                    .providerType(ProviderType.LOCAL)
                    .build();

            Member academyMember = Member.builder()
                    .username("test2")
                    .password(passwordEncoder.encode("test2"))
                    .name("훈이")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .isRoadAddress(true)
                    .role(RoleType.ROLE_ACADEMY)
                    .providerType(ProviderType.LOCAL)
                    .build();

            Member tutorMember = Member.builder()
                    .username("test3")
                    .password(passwordEncoder.encode("test3"))
                    .name("철수")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .isRoadAddress(true)
                    .role(RoleType.ROLE_TUTOR)
                    .providerType(ProviderType.LOCAL)
                    .build();

            em.persist(member);
            em.persist(academyMember);
            em.persist(tutorMember);
            em.flush();

            Academy academy = Academy.builder()
                    .member(academyMember)
                    .businessRegistration("사업자등록증 파일 경로")
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .isRoadAddress(false)
                    .name("학원이름")
                    .shuttle(true)
                    .introduce("학원 소개")
                    .educations("초등학교 5학년,초등학교 6학년,초등학교 2학년,초등학교 4학년,초등학교 3학년")
                    .subjects("독서/토론/논술,입시컨설팅,SW교육/코딩교육,제2외국어")
                    .build();

            TutorInfo tutorInfo = TutorInfo.builder()
                    .member(tutorMember)
                    .scholarship("서울대학교")
                    .certification("개인과외교습자 등록 파일")
                    .educations("중학교 1학년,중학교 2학년,중학교 3학년,고등학교 1학년,고등학교 2학년")
                    .subjects("국어,수학,영어,과학")
                    .build();

            em.persist(academy);
            em.persist(tutorInfo);
        }
    }
}
