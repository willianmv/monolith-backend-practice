package com.simple.blog.backend.core.usecases.reply.delete;

public interface IDeleteReplyUseCase {

    void execute(Long userId, Long replyId);
}
