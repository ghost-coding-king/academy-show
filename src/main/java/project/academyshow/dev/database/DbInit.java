package project.academyshow.dev.database;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.entity.Member;
import project.academyshow.entity.RoleType;
import project.academyshow.entity.Subject;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

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
                    .role(RoleType.ROLE_MEMBER)
                    .build();

            em.persist(member);
        }
    }
}
