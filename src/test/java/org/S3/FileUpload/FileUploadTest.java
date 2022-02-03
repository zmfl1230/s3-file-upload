package org.S3.FileUpload;

import org.S3.FileUpload.domain.Content;
import org.S3.FileUpload.service.S3FileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;


@SpringBootTest
class FileUploadTest {

    @Autowired
    S3FileService s3FileService;

    @Test
    public void fileUploadTest() {
        // given
        // 파일 목 객체 생성
        Content file = null;
        String originalFileName = "example.png" ;
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", originalFileName,
                "image/png", "test data".getBytes());

        // when
        // 목 파일 업로드 및 파일 객체 저장
        try {
            file = s3FileService.upload(mockMultipartFile, "files");
        } catch (IOException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }

        // then
        // 저장된 파일의 original file name이 일치하는지 검증
        if(file != null){
            System.out.println("file = " + file);
            Assertions.assertThat(originalFileName).isEqualTo(file.getOriginalFilename());
        }
    }

}
