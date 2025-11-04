package com.simple.blog.backend.core.usecases.reply.create;

public record CreateReplyInput(
        long authorId,
        long postId,
        String content
) {}
