package com.simple.blog.backend.core.usecases.reply.create;

import java.time.Instant;

public record CreateReplyOutput(
        long replyId,
        Instant createdAt,
        String replyAuthor,
        String repliedTo,
        String content
) {}
