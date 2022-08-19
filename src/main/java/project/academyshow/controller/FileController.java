package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.service.FileService;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileController {

    private final FileService fileService;

    @GetMapping(value = "/files/{id}")
    public byte[] getFile(@PathVariable("id") Long id) throws IOException {
        log.debug("getFile id = {}", id);
        return FileCopyUtils.copyToByteArray(fileService.getFile(id));
    }

    @PostMapping("/files")
    public ApiResponse<?> uploadFile(@RequestBody MultipartFile file) throws IOException {
        log.debug("uploadFile");
        return ApiResponse.success(fileService.upload(file));
    }
}

