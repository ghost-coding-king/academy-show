package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.academyshow.service.FilePath;
import project.academyshow.service.FileService;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping(value = "/files/{id}")
    public byte[] getFile(@PathVariable("id") Long id) throws IOException {
        return FileCopyUtils.copyToByteArray(fileService.getFile(id));
    }

    @PostMapping("/files")
    public String uploadFile(@RequestBody MultipartFile file) throws IOException {
        return fileService.upload(file, FilePath.FROM_POST);
    }
}
