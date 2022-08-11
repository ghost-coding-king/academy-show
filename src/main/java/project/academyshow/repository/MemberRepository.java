package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.academyshow.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
