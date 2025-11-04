package com.simple.blog.backend.infra.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReplyDTO(
        @NotNull long postId,
        @NotBlank(message = "This field cannot be empty") String content
) {}
