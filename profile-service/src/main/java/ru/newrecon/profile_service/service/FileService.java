package ru.newrecon.profile_service.service;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.profile_service.entity.File;
import ru.newrecon.profile_service.repository.FileRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Client s3Client;
    private final FileRepository fileRepository;

    @Value("${rustfs.bucket-name}")
    private String bucketName;

    public File findByUserId(UUID userId) {
        return fileRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public UUID upload(UUID userId, MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();

            if (originalName == null || !originalName.contains(".")) {
                throw new IllegalArgumentException("Некорректное имя файла");
            }

            String extension = originalName.substring(originalName.lastIndexOf("."));

            if (!List.of(".jpg", ".jpeg", ".png", ".webp").contains(extension)) {
                throw new IllegalArgumentException("Недопустимый формат файла");
            }

            UUID objectName = UUID.randomUUID();
            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(objectName.toString())
                                .contentType(file.getContentType())
                                .build(),
                        RequestBody.fromInputStream(inputStream, file.getSize()));
            }

            fileRepository.findByUserId(userId).ifPresent(oldFile -> {
                try {
                    s3Client.deleteObject(DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(oldFile.getId().toString())
                            .build());
                } catch (Exception e) {
                    log.warn("Не удалось удалить старый файл из RustFS: {}", oldFile.getId(), e);
                }
                fileRepository.delete(oldFile);
            });

            File fileEntity = new File();
            fileEntity.setId(objectName);
            fileEntity.setUserId(userId);
            fileRepository.save(fileEntity);

            log.info("Файл загружен: " + objectName);

            return objectName;
        } catch (Exception e) {
            log.error("Ошибка загрузки: " + e.getMessage(), e);
            throw new RuntimeException("Не удалось загрузить файл", e);
        }
    }
}
