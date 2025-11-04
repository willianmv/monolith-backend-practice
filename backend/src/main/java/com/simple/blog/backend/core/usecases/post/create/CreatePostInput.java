package com.simple.blog.backend.core.usecases.post.create;

import java.util.Set;

public record CreatePostInput(
        long userId,
        String title,
        String content,
        String imageUrl,
        Set<String> tagNames) {}
