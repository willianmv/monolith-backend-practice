package com.simple.blog.backend.core.usecases.reply.get;

import java.time.Instant;

public record ReplySummaryOutput(
        Long id,
        String replyAuthor,
        String content,
        Instant createdAt
) {}
