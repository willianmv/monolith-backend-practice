package com.simple.blog.backend.infra.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface IImageStorageService {

    String save(MultipartFile image);

}
