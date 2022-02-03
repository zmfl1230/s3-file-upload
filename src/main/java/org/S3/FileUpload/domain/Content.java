package org.S3.FileUpload.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Content {

    public Content(String fileUri, String fileName, String originalFilename, @Nullable String contentType) {
        this.fileUri = fileUri;
        this.fileName = fileName;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    private String fileUri;

    private String fileName;

    private String originalFilename;

    @Nullable
    private String contentType;

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", fileUri='" + fileUri + '\'' +
                ", fileName='" + fileName + '\'' +
                ", originalFilename='" + originalFilename + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
