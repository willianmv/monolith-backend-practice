package com.simple.blog.backend.infra.dto.mapper;

import com.simple.blog.backend.core.usecases.post.create.CreatePostOutput;
import com.simple.blog.backend.infra.dto.output.CreatedPostDTO;

public class PostMapperDTO {

    public static CreatedPostDTO toCreatedPostDTO(CreatePostOutput createPostOutput){
        return new CreatedPostDTO(
                createPostOutput.authorUsername(),
                createPostOutput.title(),
                createPostOutput.content(),
                createPostOutput.tags(),
                createPostOutput.imageUrl());
    }

}
