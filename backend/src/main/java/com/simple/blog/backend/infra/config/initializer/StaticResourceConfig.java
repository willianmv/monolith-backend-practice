package com.simple.blog.backend.infra.config.initializer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get("uploads", "images").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/images/**").addResourceLocations(uploadPath);
    }
}
