package project.academyshow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.entity.Academy;

public interface AcademyRepository extends JpaRepository<Academy, Long> {

    @Query(value =
            "select * " +
            "from academy " +
            "where subjects regexp(:#{#searchRequest.subjectRegexp}) " +
            "and educations regexp(:#{#searchRequest.educationRegexp}) " +
            "and jibun_address like %:#{#searchRequest.area}%", nativeQuery = true)
    Page<Academy> findAll(SearchRequest searchRequest, Pageable pageable);

    Page<Academy> findByNameContaining(String name, Pageable pageable);
}
