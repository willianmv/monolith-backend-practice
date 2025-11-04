package com.simple.blog.backend.infra.service.image;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "application.local-storage")
public class LocalImageStorageProperties {

    private String uploadDir;
    private String publicUrlPrefix;
    private int maxSizeMb;
    private List<String> allowedContentTypes;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public void setPublicUrlPrefix(String publicUrlPrefix) {
        this.publicUrlPrefix = publicUrlPrefix;
    }

    public void setMaxSizeMb(int maxSizeMb) {
        this.maxSizeMb = maxSizeMb;
    }

    public void setAllowedContentTypes(List<String> allowedContentTypes) {
        this.allowedContentTypes = allowedContentTypes;
    }

    public String getPublicUrlPrefix() {
        return publicUrlPrefix;
    }

    public int getMaxSizeMb() {
        return maxSizeMb;
    }

    public List<String> getAllowedContentTypes() {
        return allowedContentTypes;
    }

}
