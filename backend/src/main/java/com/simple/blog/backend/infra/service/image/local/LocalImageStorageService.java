package com.simple.blog.backend.infra.service.image.local;

import com.simple.blog.backend.infra.service.image.IImageService;
import com.simple.blog.backend.infra.service.image.ImageValidator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Profile("dev")
public class LocalImageStorageService implements IImageService {

    private final LocalImageStorageProperties localProperties;
    private final ImageValidator imageValidator;

    public LocalImageStorageService(LocalImageStorageProperties localProperties, ImageValidator imageValidator) {
        this.localProperties = localProperties;
        this.imageValidator = imageValidator;
    }

    @Override
    public String save(MultipartFile image) {
        imageValidator.validate(image);

        final String extension = imageValidator.getExtension(image.getOriginalFilename());
        final String fileName = UUID.randomUUID() + "." + extension;

        final Path uploadPath = Paths.get(localProperties.getUploadDir());
        final Path fullPath = uploadPath.resolve(fileName);

        try{
            Files.createDirectories(uploadPath);
            Files.copy(image.getInputStream(), fullPath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }

        return localProperties.getPublicUrlPrefix() + fileName;
    }

}
