package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.academyshow.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
