package com.simple.blog.backend.infra.dto.input;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDTO(@NotBlank(message = "This field cannot be empty") String refreshToken) {}
