package org.S3.FileUpload.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.S3.FileUpload.domain.Content;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
    /**
     * 파일 업로드 로직의 순서는 간단합니다.
     * 1. MultipartFile을 전달받고, s3에 MultipartFile 타입은 전송이 안되기 때문에 해당 파일을 File 타입으로 전환합니다.
     * 2. 전환을 위해 로컬에 헤딩 파일을 저장하고, File 타입의 객체를 반환받습니다.후에 로컬에 저장한 파일은 삭제합니다.
     * 3. 전환된 File을 S3에 읽기 권한으로 put 한 뒤, 로컬에 생성된 File을 삭제합니다.
     * 4. 업로드된 파일의 S3 주소를 반환받습니다.
     */

    private final AmazonS3Client amazonS3Client;

    private final EntityManager entityManager;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    @Transactional
    public Content upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = covertFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("File로 변환하는데 실패했습니다."));

        String fileName = dirName + "/" + uploadFile.getName();
        String uri = uploadS3(uploadFile, fileName);
        Content content = createContentEntity(uri, fileName, multipartFile.getOriginalFilename(), multipartFile.getContentType());
        entityManager.persist(content);
        return content;
    }

    private String uploadS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        removeNewFile(uploadFile);
        return getFileUrl(fileName);
    }

    private Optional<File> covertFile(MultipartFile multipartFile) throws IOException{
        if(multipartFile.getOriginalFilename() == null) return Optional.empty();
        File file = new File(createUniqueFileName(multipartFile.getOriginalFilename()));
        if (file.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }

    private String createUniqueFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 파일 형식입니다.(%s)", fileName));
        }
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Content createContentEntity(String originalFileName, String fileName, String fileUri, @Nullable String contentType) {
        return new Content(originalFileName, fileName, fileUri, contentType);
    }
}


