package project.academyshow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.academyshow.entity.ReferenceType;
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
                "and r.referenceId = :reviewedId " +
                "order by r.createdAt desc ",
        countQuery = "select count(r) " +
                     "from Review r " +
                     "where r.type = :type " +
                     "and r.referenceId = :reviewedId "
    )
    Page<Review> findAllByTypeEqualsAndReviewedIdEquals(Pageable pageable, ReferenceType type, Long reviewedId);

    @Override
    @Query("select r from Review r inner join fetch r.member where r.id = :id")
    Optional<Review> findById(Long id);

    @Query("select count(r), r.rating " +
            "from Review r " +
            "where r.type = :type " +
            "and r.referenceId = :id " +
            "group by r.rating ")
    List<Tuple> countGroupByRatingForType(ReferenceType type, Long id);
}
