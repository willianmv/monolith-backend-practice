package com.simple.blog.backend.core.usecases.post;

import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.gateway.repository.ITagRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.ILoggerService;
import com.simple.blog.backend.core.usecases.post.create.CreatePostInput;
import com.simple.blog.backend.core.usecases.post.create.CreatePostUseCase;
import com.simple.blog.backend.core.validator.CreatePostValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Create Post Use Case")
class CreatePostUseCaseTest {

    @Mock
    private IPostRepository postRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ITagRepository tagRepository;

    @Mock
    private ILoggerService loggerService;

    @Mock
    private CreatePostValidator createPostValidator;

    @InjectMocks
    private CreatePostUseCase useCase;

    @Test
    @DisplayName("Should throw when validation fails")
    void shouldThrowWhenValidationFails() {
        // Arrange
        CreatePostInput input = new CreatePostInput(
                1L, "", "", null, Set.of()
        );

        doThrow(new IllegalArgumentException("Invalid post data"))
                .when(createPostValidator).validate(input);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));

        assertEquals("Invalid post data", ex.getMessage());
        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw if user not found")
    void shouldThrowIfUserNotFound() {
        // Arrange
        CreatePostInput input = new CreatePostInput(
                999L,
                "Test Post",
                "Content",
                null,
                Set.of("TECH")
        );

        when(userRepository.findById(999L)).thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.execute(input));

        assertEquals("User not found", ex.getMessage());
        verify(postRepository, never()).save(any());
    }

}