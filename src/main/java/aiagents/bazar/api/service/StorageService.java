package aiagents.bazar.api.service;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.public-endpoint:${minio.endpoint}}")
    private String publicEndpoint;

    public String uploadImage(MultipartFile file, Long taskId) {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("File is empty or null");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String s3Key = "tasks/" + taskId + "/" + UUID.randomUUID() + extension;

            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(s3Key)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            log.info("Successfully uploaded image to MinIO: {}", s3Key);
            return s3Key;
        } catch (MinioException e) {
            log.error("MinIO error while uploading image", e);
            throw new RuntimeException("Failed to upload image to MinIO: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error while uploading image", e);
            throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
        }
    }

    public void deleteImage(String s3Key) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(s3Key)
                            .build()
            );
            log.info("Successfully deleted image from MinIO: {}", s3Key);
        } catch (MinioException e) {
            log.error("MinIO error while deleting image", e);
            throw new RuntimeException("Failed to delete image from MinIO: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error while deleting image", e);
            throw new RuntimeException("Failed to delete image: " + e.getMessage(), e);
        }
    }

    public String getPublicUrl(String s3Key) {
        try {
            // Используем прямой URL, так как bucket публичный
            // Формат: http://localhost:9000/bazar-images/tasks/1/...
            String url = publicEndpoint + "/" + bucketName + "/" + s3Key;
            log.debug("Generated public URL for image: {}", url);
            return url;
        } catch (Exception e) {
            log.error("Error while generating public URL for image", e);
            return publicEndpoint + "/" + bucketName + "/" + s3Key;
        }
    }

    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error("Error while checking bucket existence", e);
            return false;
        }
    }
}

