package org.S3.FileUpload.controller;

import lombok.RequiredArgsConstructor;
import org.S3.FileUpload.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("upload")
public class FIleUploadController {
    private final FileService fileService;

    // 단일 파일 업로드
    @PostMapping("file")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return fileService.upload(file, "images");
    }

    // 복수 파일 업로드
    @PostMapping("files")
    public String uploadImages(@RequestPart List<MultipartFile> file) {
        return null;
    }
}
