package project.academyshow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.academyshow.entity.Post;
import project.academyshow.entity.PostCategory;

public interface PostRepository extends JpaRepository<Post, Long> {
//    @Query(
//            value = "select p " +
//                    "from Post p " +
//                    "inner join fetch p.member " +
//                    "inner join fetch p.academy " +
//                    "where p.category = #{postRequest.category}",
//
//            countQuery = "select p " +
//                         "from Post p " +
//                         "where p.category = #{postRequest.category}"
//    )
//    Page<Post> findAll(PostRequest postRequest, Pageable pageable);

    @Query("select p " +
            "from Post p " +
            "where p.academy.id = :id " +
            "order by p.createdAt desc")
    Page<Post> findAllByAcademy(Long id, Pageable pageable);

    @Query("select p " +
            "from Post p " +
            "where p.tutorInfo.id = :id " +
            "order by p.createdAt desc")
    Page<Post> findAllByTutorInfo(Long id, Pageable pageable);

    Page<Post> findAllByCategoryOrderByCreatedAtDesc(PostCategory category, Pageable pageable);

//    @Query("select p " +
//            "from Post p " +
//            "where p.academy.id = :academyId " +
//            "and p.id = :postId ")
//    Optional<Post> findByAcademyAndPostId(Long academyId, Long postId);
}
