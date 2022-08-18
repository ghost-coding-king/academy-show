package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.academyshow.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
