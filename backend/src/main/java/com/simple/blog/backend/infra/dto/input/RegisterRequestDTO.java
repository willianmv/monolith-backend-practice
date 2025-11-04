package com.simple.blog.backend.infra.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "This field cannot be empty")
        @Size(min = 3, max = 100,  message = "This field must have between {min} and {max} characters")
        String name,

        @NotBlank(message = "This field cannot be empty")
        @Size(min = 3, max = 32,  message = "This field must have between {min} and {max} characters")
        String username,

        @NotBlank(message = "This field cannot be empty")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "This field cannot be empty")
        @Size(min = 6,  message = "This field must have at least {min} characters")
        String password
) {}
