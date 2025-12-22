package com.ssafy.zipcheck.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploaderService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadProfileImage(MultipartFile file, Integer userId) {

        validateImage(file);

        String fileName = createFileName(userId, file.getOriginalFilename());

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build(); // acl 제거

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(file.getBytes())
            );

        } catch (IOException e) {
            throw new IllegalStateException("S3 업로드 실패", e);
        }

        // AWS SDK가 보장하는 URL 생성 방식
        return s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucket).key(fileName))
                .toExternalForm();
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 없습니다.");
        }
        if (file.getSize() > 5_000_000) {
            throw new IllegalArgumentException("이미지는 5MB 이하만 가능합니다.");
        }
        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
    }

    private String createFileName(Integer userId, String originalFilename) {
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        return "profile/" + userId + "/" + UUID.randomUUID() + ext;
    }
}
