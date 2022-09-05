
package project.academyshow.dev.database;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.entity.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final ExampleInsert exampleInsert;

    @PostConstruct
    public void init() {
        exampleInsert.insertSubject();
        exampleInsert.insertFile();
        exampleInsert.insertNormalMember();
        exampleInsert.insertAcademyMember();
        exampleInsert.insertTutorMember();
        exampleInsert.insertAcademyReview();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class ExampleInsert {

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        private final List<Member> normalMembers = new ArrayList<>();
        private final List<Member> academyMembers = new ArrayList<>();
        private final List<Academy> academies = new ArrayList<>();

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

        public void insertFile() {
            for (int i=10000; i<10009; i++) {
                em.createNativeQuery("insert into file_info(id, ext, path, size) values(" + i + ", '.png', '', 0)")
                        .executeUpdate();
            }
        }

        public void insertNormalMember() {
            Member member1 = Member.builder()
                    .username("user1")
                    .password(passwordEncoder.encode("user1"))
                    .name("유리")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(false)
                    .profile("http://localhost:8081/api/files/10000")
                    .role(RoleType.ROLE_MEMBER)
                    .providerType(ProviderType.LOCAL)
                    .build();

            em.persist(member1);
            normalMembers.add(member1);
        }

        public void insertAcademyMember() {
            Member academyMember1 = Member.builder()
                    .username("academy1")
                    .password(passwordEncoder.encode("academy1"))
                    .name("철수")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(true)
                    .profile("http://localhost:8081/api/files/10001")
                    .role(RoleType.ROLE_ACADEMY)
                    .providerType(ProviderType.LOCAL)
                    .build();

            Member academyMember2 = Member.builder()
                    .username("academy2")
                    .password(passwordEncoder.encode("academy2"))
                    .name("두목님")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(true)
                    .profile(null)
                    .role(RoleType.ROLE_ACADEMY)
                    .providerType(ProviderType.LOCAL)
                    .build();

            Member academyMember3 = Member.builder()
                    .username("academy3")
                    .password(passwordEncoder.encode("academy3"))
                    .name("두목님2")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(true)
                    .profile(null)
                    .role(RoleType.ROLE_ACADEMY)
                    .providerType(ProviderType.LOCAL)
                    .build();

            Member academyMember4 = Member.builder()
                    .username("academy4")
                    .password(passwordEncoder.encode("academy4"))
                    .name("두목님3")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(true)
                    .profile(null)
                    .role(RoleType.ROLE_ACADEMY)
                    .providerType(ProviderType.LOCAL)
                    .build();

            Member academyMember5 = Member.builder()
                    .username("academy5")
                    .password(passwordEncoder.encode("academy5"))
                    .name("두목님4")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(true)
                    .profile(null)
                    .role(RoleType.ROLE_ACADEMY)
                    .providerType(ProviderType.LOCAL)
                    .build();

            Member academyMember6 = Member.builder()
                    .username("academy4")
                    .password(passwordEncoder.encode("academy4"))
                    .name("두목님5")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(true)
                    .profile(null)
                    .role(RoleType.ROLE_ACADEMY)
                    .providerType(ProviderType.LOCAL)
                    .build();

            em.persist(academyMember1);    academyMembers.add(academyMember1);
            em.persist(academyMember2);    academyMembers.add(academyMember2);
            em.persist(academyMember3);    academyMembers.add(academyMember3);
            em.persist(academyMember4);    academyMembers.add(academyMember4);
            em.persist(academyMember5);    academyMembers.add(academyMember5);
            em.persist(academyMember6);    academyMembers.add(academyMember6);
            em.flush();

            Academy academy1 = Academy.builder()
                    .member(academyMember1)
                    .businessRegistration("사업자등록증 파일 경로")
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(false)
                    .name("수학연구소")
                    .shuttle(true)
                    .introduce("최고보다 최선을 다하겠습니다.")
                    .educations("고등학교 1학년,고등학교 2학년,고등학교 3학년")
                    .subjects("수학")
                    .profile("http://localhost:8081/api/files/10003")
                    .build();

            Academy academy2 = Academy.builder()
                    .member(academyMember2)
                    .businessRegistration("사업자등록증 파일 경로")
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(false)
                    .name("축지법 학원")
                    .shuttle(true)
                    .introduce("대한축지법협회 41대 회장 출신 원장 직강")
                    .educations("중학교 1학년,중학교 2학년,중학교 3학년,고등학교 1학년,고등학교 2학년,고등학교 3학년,성인")
                    .subjects("체육")
                    .profile("http://localhost:8081/api/files/10004")
                    .build();

            Academy academy3 = Academy.builder()
                    .member(academyMember3)
                    .businessRegistration("사업자등록증 파일 경로")
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(false)
                    .name("부두술 전문학원")
                    .shuttle(true)
                    .introduce("부두술사관학교 출신 전문교사들이 가르칩니다.")
                    .educations("중학교 1학년,중학교 2학년,중학교 3학년")
                    .subjects("기타")
                    .profile("http://localhost:8081/api/files/10005")
                    .build();

            Academy academy4 = Academy.builder()
                    .member(academyMember4)
                    .businessRegistration("사업자등록증 파일 경로")
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(false)
                    .name("탄탄 비행술")
                    .shuttle(true)
                    .introduce("아직도 걸어다니십니까? 이제는 날아다니는게 대세!")
                    .educations("고등학교 1학년,고등학교 2학년,고등학교 3학년,성인")
                    .subjects("체육")
                    .profile("http://localhost:8081/api/files/10006")
                    .build();

            Academy academy5 = Academy.builder()
                    .member(academyMember5)
                    .businessRegistration("사업자등록증 파일 경로")
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(false)
                    .name("슈퍼 영어학원")
                    .shuttle(true)
                    .introduce("Hello. Nice to meet you, and you?")
                    .educations("고등학교 1학년,고등학교 2학년,고등학교 3학년,성인")
                    .subjects("영어")
                    .profile("http://localhost:8081/api/files/10007")
                    .build();

            Academy academy6 = Academy.builder()
                    .member(academyMember6)
                    .businessRegistration("사업자등록증 파일 경로")
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(false)
                    .name("펑펑 미술학원")
                    .shuttle(true)
                    .introduce("폭발은 예술이다.")
                    .educations("고등학교 1학년,고등학교 2학년,고등학교 3학년,성인")
                    .subjects("미술")
                    .profile("http://localhost:8081/api/files/10008")
                    .build();

            em.persist(academy1);   academies.add(academy1);
            em.persist(academy2);   academies.add(academy2);
            em.persist(academy3);   academies.add(academy3);
            em.persist(academy4);   academies.add(academy4);
            em.persist(academy5);   academies.add(academy5);
            em.persist(academy6);   academies.add(academy6);
        }

        public void insertTutorMember() {
            Member tutorMember1 = Member.builder()
                    .username("tutor1")
                    .password(passwordEncoder.encode("tutor1"))
                    .name("수지")
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode("13524")
                    .roadAddress("경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)")
                    .jibunAddress("경기 성남시 분당구 삼평동 653")
                    .selectRoadAddress(true)
                    .profile("http://localhost:8081/api/files/10002")
                    .role(RoleType.ROLE_TUTOR)
                    .providerType(ProviderType.LOCAL)
                    .build();

            em.persist(tutorMember1);
            em.flush();

            TutorInfo tutorInfo1 = TutorInfo.builder()
                    .member(tutorMember1)
                    .scholarship("서울대학교")
                    .certification("개인과외교습자 등록 파일 경로")
                    .educations("중학교 1학년,중학교 2학년,중학교 3학년,고등학교 1학년,고등학교 2학년")
                    .subjects("영어")
                    .build();

            em.persist(tutorInfo1);
        }

        /** 아카데미 리뷰 데이터 */
        public void insertAcademyReview() {
            Review review1 = Review.builder()
                    .type(ReferenceType.ACADEMY)
                    .reviewedId(academies.get(0).getId())
                    .member(normalMembers.get(0))
                    .reviewAge("고등학교 1학년")
                    .comment("참 못 가르쳐요")
                    .rating(1)
                    .build();

            Review review2 = Review.builder()
                    .type(ReferenceType.ACADEMY)
                    .reviewedId(academies.get(0).getId())
                    .member(academyMembers.get(0))
                    .reviewAge("고등학교 2학년")
                    .comment("의사양반 이게 무슨소리야 못가르친다니 내가 0점이라니")
                    .rating(2)
                    .build();

            Review review3 = Review.builder()
                    .type(ReferenceType.ACADEMY)
                    .reviewedId(academies.get(0).getId())
                    .member(academyMembers.get(1))
                    .reviewAge("중학교 1학년")
                    .comment("선생님이 잘생겻어요")
                    .rating(5)
                    .build();

            Review review4 = Review.builder()
                    .type(ReferenceType.ACADEMY)
                    .reviewedId(academies.get(1).getId())
                    .member(academyMembers.get(2))
                    .reviewAge("중학교 3학년")
                    .comment("이 사람 사기꾼입니다 밎지 마세요")
                    .rating(2)
                    .build();

            Review review5 = Review.builder()
                    .type(ReferenceType.ACADEMY)
                    .reviewedId(academies.get(2).getId())
                    .member(academyMembers.get(3))
                    .reviewAge("고등학교 1학년")
                    .comment("역시 부두술 전문가!!")
                    .rating(4)
                    .build();

            Review review6 = Review.builder()
                    .type(ReferenceType.ACADEMY)
                    .reviewedId(academies.get(3).getId())
                    .member(academyMembers.get(4))
                    .reviewAge("고등학교 2학년")
                    .comment("이제 비행기 없어도 해외여행 쌉가능")
                    .rating(1)
                    .build();

            Review review7 = Review.builder()
                    .type(ReferenceType.ACADEMY)
                    .reviewedId(academies.get(4).getId())
                    .member(academyMembers.get(5))
                    .reviewAge("고등학교 2학년")
                    .comment("I cant speak english")
                    .rating(5)
                    .build();

            Review review8 = Review.builder()
                    .type(ReferenceType.ACADEMY)
                    .reviewedId(academies.get(5).getId())
                    .member(academyMembers.get(5))
                    .reviewAge("성인")
                    .comment("링가르디움 레비오우사")
                    .rating(2)
                    .build();

            em.persist(review1);
            em.persist(review2);
            em.persist(review3);
            em.persist(review4);
            em.persist(review5);
            em.persist(review6);
            em.persist(review7);
            em.persist(review8);
        }
    }
}
