package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.academyshow.entity.Up;
import project.academyshow.entity.Member;
import project.academyshow.entity.ReferenceType;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Up, Long> {
    Optional<Up> findByTypeAndReferenceIdAndMember(ReferenceType type, Long referenceId, Member member);

    Long countByTypeAndReferenceId(ReferenceType type, Long referenceId);
}
