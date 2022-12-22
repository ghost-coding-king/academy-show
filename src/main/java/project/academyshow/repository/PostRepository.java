package project.academyshow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.academyshow.entity.Member;
import project.academyshow.entity.Post;
import project.academyshow.entity.PostCategory;

public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * 여러 post를 조회한다.
     * @param pageable
     * @return
     */
    @Query(
            value = "select p " +
                    "from Post p " +
                    "inner join fetch p.batchLikes " +
                    "inner join fetch p.member " +
                    "left join fetch p.academy " +
                    "left join fetch p.tutorInfo ",

            countQuery = "select count(p) " +
                    "from Post p "
    )
    Page<Post> findAll(Pageable pageable);

    @Query("select l.post " +
            "from Likes l " +
            "where l.post = :post " +
            "and l.member = :member")
    Post likedByMemberAndInPost(Member member, Post post);

    @Query("select p " +
            "from Post p " +
            "where p.academy.id = :id " +
            "order by p.createdAt desc")
    Page<Post> findAllByAcademyId(Long id, Pageable pageable);

    @Query("select p " +
            "from Post p " +
            "where p.tutorInfo.id = :id " +
            "order by p.createdAt desc")
    Page<Post> findAllByTutorInfoId(Long id, Pageable pageable);

    Page<Post> findAllByCategoryOrderByCreatedAtDesc(PostCategory category, Pageable pageable);

//    @Query("select p " +
//            "from Post p " +
//            "where p.academy.id = :academyId " +
//            "and p.id = :postId ")
//    Optional<Post> findByAcademyAndPostId(Long academyId, Long postId);
}
