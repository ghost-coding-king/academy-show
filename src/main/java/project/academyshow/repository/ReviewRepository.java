package project.academyshow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.academyshow.entity.Review;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(
        value = "select r " +
                "from Review r " +
                "inner join fetch r.member " +
                "where r.type = :type " +
                "and r.reviewedId = :reviewedId " +
                "order by r.createdAt desc ",
        countQuery = "select count(r) " +
                     "from Review r " +
                     "where r.type = :type " +
                     "and r.reviewedId = :reviewedId "
    )
    Page<Review> findAllByTypeEqualsAndReviewedIdEquals(Pageable pageable, Review.TYPE type, Long reviewedId);

    @Override
    @Query("select r from Review r inner join fetch r.member where r.id = :id")
    Optional<Review> findById(Long id);

    @Query("select count(r), r.rating " +
            "from Review r " +
            "where r.type = :type " +
            "and r.reviewedId = :id " +
            "group by r.rating ")
    List<Tuple> countGroupByRatingForType(Review.TYPE type, Long id);
}
