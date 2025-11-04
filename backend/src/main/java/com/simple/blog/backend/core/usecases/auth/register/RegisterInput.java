package com.simple.blog.backend.core.usecases.auth.register;

public record RegisterInput(
        String name,
        String username,
        String email,
        String rawPassword
) {}
