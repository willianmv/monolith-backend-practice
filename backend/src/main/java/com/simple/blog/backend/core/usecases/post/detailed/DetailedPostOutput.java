package com.simple.blog.backend.core.usecases.post.detailed;

import java.time.Instant;
import java.util.Set;

public record DetailedPostOutput(
        long id,
        String authorUsername,
        String title,
        String content,
        String imageUrl,
        Set<String> tags,
        int replyCount,
        Instant createdAt
) {}
