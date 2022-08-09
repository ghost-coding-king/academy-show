package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.academyshow.entity.FileInfo;
import project.academyshow.repository.FileInfoRepository;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileInfoRepository fileInfoRepository;

    public String upload(MultipartFile file, FilePath path) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        // 확장자 추출
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        // FileInfo Entity 생성
        FileInfo fileInfo = FileInfo.builder()
                .path(path)
                .size(file.getSize())
                .ext(ext).build();

        // Insert
        fileInfoRepository.save(fileInfo);
        // File upload
        File newFile = new File(path.getPath(), fileInfo.getId() + ext);
        if (!newFile.exists()) newFile.mkdirs();
        file.transferTo(newFile);

        log.debug("upload, saved id = {}", fileInfo.getId());

        return "http://localhost:8081/files/" + fileInfo.getId();
    }

    public File getFile(Long id) {
        Optional<FileInfo> fileInfo = fileInfoRepository.findById(id);
        if (fileInfo.isPresent()) {
            FileInfo f = fileInfo.get();
            log.debug("getFile id = {}", id);
            return new File(f.getPath().getPath(), f.getId() + f.getExt());
        }
        log.warn("getFile, id({}) not exist", id);
        return null;
    }
}
