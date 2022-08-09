package project.academyshow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.academyshow.service.FileService;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/files/{id}")
    public File getFile(@PathVariable("id") Long id) {
        return fileService.getFile(id);
    }
}
