package ru.newrecon.profile_service.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutBucketPolicyRequest;

@Slf4j
@Configuration
public class RustFSConfig {

    @Value("${rustfs.endpoint}")
    private String endpoint;

    @Value("${rustfs.access-key}")
    private String accessKey;

    @Value("${rustfs.secret-key}")
    private String secretKey;

    @Value("${rustfs.bucket-name}")
    private String bucketName;

    @Bean
    public S3Client s3Client() {
        S3Client client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .forcePathStyle(true)
                .build();

        ensureBucketExists(client);
        applyPublicReadPolicy(client);
        return client;
    }

    private void ensureBucketExists(S3Client client) {
        String policy = """
        {
          "Version": "2012-10-17",
          "Statement": [
            {
              "Effect": "Allow",
              "Principal": "*",
              "Action": "s3:GetObject",
              "Resource": "arn:aws:s3:::%s/*"
            }
          ]
        }
        """.formatted(bucketName);

        try {
            client.headBucket(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            log.info("Bucket уже существует: {}", bucketName);
        } catch (NoSuchBucketException e) {
            try {
                client.createBucket(CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Bucket создан: {}", bucketName);
            } catch (BucketAlreadyOwnedByYouException ex) {
                log.info("Bucket уже создан параллельно: {}", bucketName);
            } catch (Exception ex) {
                log.error("Не удалось создать bucket {}", bucketName, ex);
            }
        } catch (Exception e) {
            log.error("Ошибка при проверке bucket {}", bucketName, e);
        }
    }

    private void applyPublicReadPolicy(S3Client client) {
        String policy = """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Effect": "Allow",
                      "Principal": "*",
                      "Action": "s3:GetObject",
                      "Resource": "arn:aws:s3:::%s/*"
                    }
                  ]
                }
                """.formatted(bucketName);

        try {
            client.putBucketPolicy(PutBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .policy(policy)
                    .build());
            log.info("Публичная политика установлена на bucket: {}", bucketName);
        } catch (Exception e) {
            log.error("Не удалось установить политику на bucket {}", bucketName, e);
        }

    }
}