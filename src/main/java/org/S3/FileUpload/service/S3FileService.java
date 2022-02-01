package org.S3.FileUpload.service;

import lombok.RequiredArgsConstructor;
import org.S3.FileUpload.util.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {
    private final S3Uploader s3Uploader;

    @Override
    public String upload(MultipartFile file, String dirName) throws IOException {
        return s3Uploader.upload(file, dirName);
    }

    @Override
    public String getFileUrl(String fileName) {
        return s3Uploader.getFileUrl(fileName);
    }
}
