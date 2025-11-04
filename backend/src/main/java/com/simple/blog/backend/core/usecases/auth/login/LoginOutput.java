package com.simple.blog.backend.core.usecases.auth.login;

public record LoginOutput(
        String accessToken,
        String refreshToken
) {}
