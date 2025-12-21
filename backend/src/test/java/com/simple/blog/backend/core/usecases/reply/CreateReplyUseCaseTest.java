package com.simple.blog.backend.core.usecases.reply;

import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.Reply;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.event.ReplyToPostEvent;
import com.simple.blog.backend.core.gateway.IPostRepository;
import com.simple.blog.backend.core.gateway.IReplyRepository;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.IEventPublisher;
import com.simple.blog.backend.core.gateway.ILoggerService;
import com.simple.blog.backend.core.usecases.reply.create.CreateReplyInput;
import com.simple.blog.backend.core.usecases.reply.create.CreateReplyOutput;
import com.simple.blog.backend.core.usecases.reply.create.CreateReplyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateReplyUseCase Test")
class CreateReplyUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IPostRepository postRepository;

    @Mock
    private IReplyRepository replyRepository;

    @Mock
    private IEventPublisher eventPublisher;

    @Mock
    private ILoggerService loggerService;

    @InjectMocks
    private CreateReplyUseCase useCase;

    private User replyAuthor;
    private User postAuthor;
    private Post post;

    @BeforeEach
    void setup() {
        replyAuthor = new User();
        replyAuthor.setId(1L);
        replyAuthor.setUsername("replyUser");

        postAuthor = new User();
        postAuthor.setId(2L);
        postAuthor.setUsername("postUser");

        post = Post.create("Post Title", "Post Content", null, postAuthor, null);
        post.setId(10L);
        post.setCreatedAt(Instant.now());
    }

    @Test
    @DisplayName("Should create reply successfully")
    void shouldCreateReplySuccessfully() {
        // Arrange
        CreateReplyInput input = new CreateReplyInput(1L, 10L, "This is a reply");

        Reply reply = Reply.create(input.content(), post, replyAuthor);
        reply.setId(100L);
        reply.setCreatedAt(Instant.now());

        when(userRepository.findById(replyAuthor.getId())).thenReturn(replyAuthor);
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        // Act
        CreateReplyOutput output = useCase.execute(input);

        // Assert
        assertNotNull(output);
        assertEquals(reply.getId(), output.replyId());
        assertEquals(replyAuthor.getUsername(), output.replyAuthor());
        assertEquals(post.getTitle(), output.repliedTo());
        assertEquals(input.content(), output.content());
        assertNotNull(output.createdAt());

        verify(userRepository).findById(replyAuthor.getId());
        verify(postRepository).findById(post.getId());
        verify(replyRepository).save(any(Reply.class));
        verify(eventPublisher).publish(any(ReplyToPostEvent.class));
    }


}