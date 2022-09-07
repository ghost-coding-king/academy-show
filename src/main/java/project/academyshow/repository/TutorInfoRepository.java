package project.academyshow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.entity.TutorInfo;

public interface TutorInfoRepository extends JpaRepository<TutorInfo, Long> {
    @Query(value =
            "select * " +
            "from tutor_info " +
            "where is_exposed = true " +
            "and area like %:#{#searchRequest.area}% " +
            "and subjects regexp(:#{#searchRequest.subjectRegexp}) " +
            "and educations regexp(:#{#searchRequest.educationRegexp})", nativeQuery = true)
    Page<TutorInfo> findAll(SearchRequest searchRequest, Pageable pageable);

    @Query(value =
            "select t " +
            "from TutorInfo t " +
            "where t.title like %:name% " +
            "or t.member in ( " +
                "select m " +
                "from Member m " +
                "where m.name like %:name% )"
    )
    Page<TutorInfo> findByQuery(String name, Pageable pageable);
}
