package com.simple.blog.backend.core.usecases.auth.login;

public record LoginInput(
        String email,
        String password
) {}
