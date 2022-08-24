package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.academyshow.entity.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
}
