package com.simple.blog.backend.infra.dto.output;

public record LoginDTO(
        String accessToken,
        String refreshToken
) {}
