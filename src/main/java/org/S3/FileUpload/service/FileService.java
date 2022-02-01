package org.S3.FileUpload.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {
    String upload(MultipartFile file, String dirName) throws IOException;
    String getFileUrl(String fileName);
}
