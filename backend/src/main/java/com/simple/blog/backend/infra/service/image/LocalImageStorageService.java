package com.simple.blog.backend.infra.service.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalImageStorageService implements IImageStorageService {

    private final LocalImageStorageProperties properties;

    public LocalImageStorageService(LocalImageStorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public String save(MultipartFile image) {
        validate(image);

        final String extension = getExtension(image.getOriginalFilename());
        final String fileName = UUID.randomUUID() + "." + extension;

        final Path uploadPath = Paths.get(properties.getUploadDir());
        final Path fullPath = uploadPath.resolve(fileName);

        try{
            Files.createDirectories(uploadPath);
            Files.copy(image.getInputStream(), fullPath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }

        return properties.getPublicUrlPrefix() + fileName;
    }

    private void validate(MultipartFile image){
        long maxBytes = properties.getMaxSizeMb() * 1024 * 1024L ;

        if(image.getSize() > maxBytes) throw new IllegalArgumentException("File too large");

        final String contentType = image.getContentType();
        if(!properties.getAllowedContentTypes().contains(contentType))
            throw new IllegalArgumentException("Unsupported file type");
    }

    private String getExtension(String fileName){
        if(fileName == null) return "bin";
        int index = fileName.lastIndexOf('.');
        return index == -1 ? "bin" : fileName.substring(index+1);
    }


}
