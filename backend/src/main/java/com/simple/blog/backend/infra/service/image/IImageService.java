package com.simple.blog.backend.infra.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {

    String save(MultipartFile image);

}
