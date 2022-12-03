package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.academyshow.entity.BatchLikes;

public interface BatchLikesRepository extends JpaRepository<BatchLikes, Long> {
}
