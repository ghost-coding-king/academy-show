package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.academyshow.entity.TutorInfo;

public interface TutorInfoRepository extends JpaRepository<TutorInfo, Long> {
}
