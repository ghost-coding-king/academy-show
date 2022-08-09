package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.academyshow.entity.FileInfo;
import project.academyshow.entity.Post;
import project.academyshow.repository.FileInfoRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileInfoRepository fileInfoRepository;

    public boolean upload(List<MultipartFile> files, FilePath path, Post ref) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return true;
            }
            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;
            // 확장자 추출
            String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
            // FileInfo Entity 생성
            FileInfo fileInfo = FileInfo.builder()
                    .originalFileName(file.getOriginalFilename())
                    .path(path)
                    .size(file.getSize())
                    .ext(ext).build();

            // Insert
            fileInfoRepository.save(fileInfo);

            // File upload
            File newFile = new File(path.getPath(), fileInfo.getId() + ext);
            file.transferTo(newFile);
        }
        return true;
    }

    public File getFile(Long id) {
        Optional<FileInfo> byId = fileInfoRepository.findById(id);
        if (byId.isPresent()) {
            FileInfo f = byId.get();
            return new File(f.getPath().getPath(), f.getId() + f.getExt());
        }
        return null;
    }
}
