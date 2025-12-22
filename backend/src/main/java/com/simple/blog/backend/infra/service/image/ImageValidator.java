package com.simple.blog.backend.infra.service.image;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageValidator {

    private final ImageProperties imageProperties;

    public ImageValidator(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    public void validate(MultipartFile image){
        long maxBytes = imageProperties.getMaxSizeMb() * 1024L * 1024L;

        if(image.getSize() > maxBytes)
            throw new IllegalArgumentException("File too large");

        if(!imageProperties.getAllowedContentTypes().contains(image.getContentType()))
            throw new IllegalArgumentException("Unsupported file type");
    }

    public String getExtension(String filename){
        if(filename == null) return "bin";
        int index = filename.lastIndexOf('.');
        return index == -1 ? "bin" : filename.substring(index + 1);
    }

}
