package project.academyshow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.academyshow.controller.request.PostRequest;
import project.academyshow.entity.Post;
import project.academyshow.entity.Review;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(
            value = "select p " +
                    "from Post p " +
                    "inner join fetch p.member " +
                    "inner join fetch p.academy " +
                    "where p.category = #{postRequest.category}",

            countQuery = "select p " +
                         "from Post p " +
                         "where p.category = #{postRequest.category}"
    )
    Page<Review> findAll(PostRequest postRequest, Pageable pageable);
}
