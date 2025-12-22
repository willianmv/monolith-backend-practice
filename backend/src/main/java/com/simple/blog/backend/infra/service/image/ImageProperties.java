package com.simple.blog.backend.infra.service.image;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "application.image")
public class ImageProperties {

    private int maxSizeMb;
    private List<String> allowedContentTypes;

    public void setMaxSizeMb(int maxSizeMb) {
        this.maxSizeMb = maxSizeMb;
    }

    public void setAllowedContentTypes(List<String> allowedContentTypes) {
        this.allowedContentTypes = allowedContentTypes;
    }

    public int getMaxSizeMb() {
        return maxSizeMb;
    }

    public List<String> getAllowedContentTypes() {
        return allowedContentTypes;
    }

}
