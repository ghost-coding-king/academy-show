package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.academyshow.entity.Academy;

public interface AcademyRepository extends JpaRepository<Academy, Long> {
}
