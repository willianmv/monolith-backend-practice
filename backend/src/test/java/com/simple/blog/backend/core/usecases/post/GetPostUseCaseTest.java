package com.simple.blog.backend.core.usecases.post;

import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.gateway.IPostRepository;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.ILoggerService;
import com.simple.blog.backend.core.usecases.post.detailed.DetailedPostOutput;
import com.simple.blog.backend.core.usecases.post.detailed.GetPostUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetPostUseCase Test")
class GetPostUseCaseTest {

    @Mock
    private IPostRepository postRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ILoggerService loggerService;

    @InjectMocks
    private GetPostUseCase useCase;

    private User author;
    private Post post;

    @BeforeEach
    void setup() {
        // Autor do post
        author = new User();
        author.setId(1L);
        author.setUsername("user123");

        // Post
        post = Post.create("Test Title", "Test Content", "image.png", author,null);
        post.setId(100L);
        post.setCreatedAt(Instant.now());
    }

    @Test
    @DisplayName("Should get detailed post successfully")
    void shouldGetDetailedPostSuccessfully() {
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(userRepository.findById(author.getId())).thenReturn(author);

        DetailedPostOutput output = useCase.execute(post.getId());

        assertNotNull(output);
        assertEquals(post.getId(), output.id());
        assertEquals(author.getUsername(), output.authorUsername());
        assertEquals(post.getTitle(), output.title());
        assertEquals(post.getContent(), output.content());
        assertEquals(post.getImageUrl(), output.imageUrl());
        assertEquals(post.getReplies().size(), output.replyCount());
        assertEquals(post.getCreatedAt(), output.createdAt());

        verify(postRepository).findById(post.getId());
        verify(userRepository).findById(author.getId());
    }

}