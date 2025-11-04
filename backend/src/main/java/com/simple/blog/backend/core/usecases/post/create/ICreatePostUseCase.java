package com.simple.blog.backend.core.usecases.post.create;

public interface ICreatePostUseCase {

    CreatePostOutput execute(CreatePostInput createPostInput);

}
