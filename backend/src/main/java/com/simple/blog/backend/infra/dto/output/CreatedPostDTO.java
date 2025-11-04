package com.simple.blog.backend.infra.dto.output;

import java.util.Set;

public record CreatedPostDTO(
        String authorUsername,
        String title,
        String content,
        Set<String> tags,
        String imageUrl
) {}
