package project.academyshow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AcademyShowApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcademyShowApplication.class, args);
    }

}
