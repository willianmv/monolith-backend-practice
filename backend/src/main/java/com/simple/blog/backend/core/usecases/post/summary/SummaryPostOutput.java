package com.simple.blog.backend.core.usecases.post.summary;

import java.time.Instant;
import java.util.Set;

public record SummaryPostOutput(
        long id,
        String title,
        String authorUsername,
        String imageUrl,
        int replyCount,
        Set<String> tags,
        Instant createdAt
) {}
