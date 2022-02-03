# S3 파일 업로드

## ✔️ 소개
profile 변경만으로 컨텐츠(파일/이미지)가 업로드 환경에 따라 서로 다른 서버에 업로드 되도록 합니다.  
➿ `배포 환경`의 경우, 실제 `AWS S3 서버`에 이미지 및 파일이 업로드 됩니다.  
➿ `개발 및 테스트 환경`의 경우, `Embedded mock S3 서버`에 이미지 및 파일이 업로드 됩니다.

## ✔️ 개발 목적
개발 환경에서 어플리케이션을 실행하거나 전반적인 구동을 테스트 할때에도 실제 AWS S3에 이미지 및 파일을 업로드하는 것은 다음과 같은 이유로 비효율적입니다.
- 자원의 낭비
  (예) 비용 청구 
- 협업 시, credentials(key) 관리의 어려움(혹은, 팀원 별 버킷 접근 권한이 허용된 IAM 사용자 생성 및 키 값 발급 등의 번거로움)의 문제

하지만, 개발 환경 혹은 테스트 환경에서만 S3가 아닌 local fileSystem으로 파일 업로드 location을 분리하자니 
S3에 업로드하는 코드와는 별개로 file system에 업로드하는 코드를 다시 작성해야 한다는 불편함이 있었습니다.

**이러한 이유로 개발 환경 혹은 테스트 환경에서는 S3 mock 서버를 띄워 이 서버에 대한 `AmazonS3Client` 빈이 생성 및 주입되도록 하여 실제 AWS S3 서버가 아닌 S3 mock 서버의 버킷으로 컨텐츠가 업로드 되도록 개발하였습니다.**

## ✔️ 사용 라이브러리 및 스텍

- MYSQL
```shell
runtimeOnly 'mysql:mysql-connector-java'
```
### ➿ 개발 환경 및 테스트 환경
- [S3 Mock](https://github.com/findify/s3mock)
```shell
implementation 'io.findify:s3mock_2.13:0.2.6'
```

**테스트 환경에서는 `mysql`이 아닌 내장 `H2 database`를 사용합니다.**

### ➿ 배포 환경
- AWS S3
```shell
implementation 'org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE'
```

- Spring boot, Gradle, JPA, Hibernate

