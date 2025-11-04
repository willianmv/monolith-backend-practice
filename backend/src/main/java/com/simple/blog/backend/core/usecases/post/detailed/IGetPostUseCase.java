package com.simple.blog.backend.core.usecases.post.detailed;

public interface IGetPostUseCase {

    DetailedPostOutput execute(Long postId);

}
