package org.S3.FileUpload.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Profile({"local"})
@Configuration
public class EmbeddedS3Config {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.mock.port}")
    private int port;


    @Bean
    public S3Mock s3mock() {
        return new S3Mock.Builder()
                .withPort(port)
                .withInMemoryBackend()
                .build();
    }

    /**
     * 의존성 주입이 완료된 이후 임베디드 S3 서버를 가동시킵니다.
     */
    @PostConstruct
    public void startS3Mock() {
        this.s3mock().start();
        log.info("인메모리 S3 Mock 서버가 시작됩니다. port: {}", port);
    }

    @PreDestroy
    public void destroyS3Mock() {
        this.s3mock().shutdown();
        log.info("인메모리 S3 Mock 서버가 종료됩니다. port: {}", port);
    }

    @Bean
    @Primary
    public AmazonS3Client amazonS3Client() {
        EndpointConfiguration endpoint = new EndpointConfiguration(getDynamicUri(), region);
        AmazonS3Client client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();

        client.createBucket(bucket);
        return client;
    }

    private String getDynamicUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .build()
                .toUriString();
    }

}
