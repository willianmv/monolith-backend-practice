package com.simple.blog.backend.core.usecases.post.create;

import java.time.Instant;
import java.util.Set;

public record CreatePostOutput(
        long id,
        long userId,
        String authorUsername,
        Instant createdAt,
        String title,
        Set<String> tags,
        String content,
        String imageUrl) {}
