package com.simple.blog.backend.infra.service.image.s3;

import com.simple.blog.backend.infra.config.aws.AwsProperties;
import com.simple.blog.backend.infra.service.image.IImageService;
import com.simple.blog.backend.infra.service.image.ImageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@Profile("prod")
public class S3ImageStorageService implements IImageService {

    private static final Logger log = LoggerFactory.getLogger(S3ImageStorageService.class);
    private final S3Client s3Client;
    private final AwsProperties awsProperties;
    private final ImageValidator imageValidator;


    public S3ImageStorageService(S3Client s3Client, AwsProperties awsProperties, ImageValidator imageValidator) {
        this.s3Client = s3Client;
        this.awsProperties = awsProperties;
        this.imageValidator = imageValidator;
    }

    @Override
    public String save(MultipartFile image) {
        log.info("[S3 Service] - Fazendo upload de imagem: {}", image.getOriginalFilename());

        imageValidator.validate(image);

        String extension = imageValidator.getExtension(image.getOriginalFilename());
        String key = "images/" + UUID.randomUUID() + "." + extension;

        try{

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(awsProperties.getS3().getBucketName())
                            .key(key)
                            .contentType(image.getContentType())
                            .acl("public-read")
                            .build(),
                    RequestBody.fromBytes(image.getBytes())
            );

        }catch (IOException ex){
            log.error("[S3 Service] - Falha ao enviar imagem para S3", ex);
            throw new RuntimeException("Failed to upload image to s3", ex);
        }

        String url =  "https://"+awsProperties.getS3().getBucketName()+".s3."+awsProperties.getS3().getRegion()+".amazonaws.com/"+key;
        log.info("[S3 Service] - Upload realizado com sucesso! URL: {}", url);
        return url;
    }


}
