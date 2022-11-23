package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.academyshow.entity.Likes;
import project.academyshow.entity.Member;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPost_IdAndMember(Long postId, Member member);

    Optional<Likes> findByTutorInfo_IdAndMember(Long tutorId, Member member);

    Optional<Likes> findAcademyLikesByAcademy_IdAndMember(Long academyId, Member member);

    Long countByTutorInfo_Id(Long referenceId);

    Long countByAcademy_Id(Long referenceId);

    Long countByPost_Id(Long referenceId);
}
