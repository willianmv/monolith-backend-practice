package com.simple.blog.backend.core.usecases.post.delete;

public interface IDeletePostUseCase {

    void execute(Long postId, Long userId);

}
