package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.academyshow.entity.Academy;

@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {
}
