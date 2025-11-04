package com.simple.blog.backend.infra.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotBlank(message = "This field cannot be empty")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "This field cannot be empty")
        @Size(min = 6,  message = "This field must have at least {min} characters")
        String password
) {}
