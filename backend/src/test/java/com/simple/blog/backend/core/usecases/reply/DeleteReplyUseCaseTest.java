package com.simple.blog.backend.core.usecases.reply;

import com.simple.blog.backend.core.domain.*;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.IPostRepository;
import com.simple.blog.backend.core.gateway.IReplyRepository;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.ILoggerService;
import com.simple.blog.backend.core.usecases.reply.delete.DeleteReplyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteReplyUseCase Test")
class DeleteReplyUseCaseTest {

    @Mock
    private IReplyRepository replyRepository;

    @Mock
    private IPostRepository postRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ILoggerService loggerService;

    @InjectMocks
    private DeleteReplyUseCase useCase;

    private User replyAuthor;
    private User postAuthor;
    private User adminUser;
    private Post post;
    private Reply reply;

    @BeforeEach
    void setup() {
        replyAuthor = new User();
        replyAuthor.setId(1L);
        replyAuthor.setUsername("replyUser");

        postAuthor = new User();
        postAuthor.setId(2L);
        postAuthor.setUsername("postUser");

        adminUser = new User();
        adminUser.setId(3L);
        adminUser.setUsername("adminUser");
        adminUser.setProfiles(Set.of(new Profile(RoleType.ADMIN, "Admin")));

        post = Post.create("Post Title", "Post Content", null, postAuthor, null);
        post.setId(10L);

        reply = Reply.create("This is a reply", post, replyAuthor);
        reply.setId(100L);
        reply.setCreatedAt(Instant.now());
    }

    @Test
    @DisplayName("Should delete reply successfully when user is author")
    void shouldDeleteReplyAsAuthor() {
        // Arrange
        when(replyRepository.findById(reply.getId())).thenReturn(reply);
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(userRepository.findById(replyAuthor.getId())).thenReturn(replyAuthor);

        // Act
        useCase.execute(replyAuthor.getId(), reply.getId());

        // Assert
        assertTrue(reply.isDeleted());
        assertEquals(replyAuthor.getId(), reply.getUpdatedBy());
        verify(replyRepository).save(reply);
    }

    @Test
    @DisplayName("Should delete reply successfully when user is post author")
    void shouldDeleteReplyAsPostAuthor() {
        // Arrange
        when(replyRepository.findById(reply.getId())).thenReturn(reply);
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(userRepository.findById(postAuthor.getId())).thenReturn(postAuthor);

        // Act
        useCase.execute(postAuthor.getId(), reply.getId());

        // Assert
        assertTrue(reply.isDeleted());
        assertEquals(postAuthor.getId(), reply.getUpdatedBy());
        verify(replyRepository).save(reply);
    }

    @Test
    @DisplayName("Should delete reply successfully when user is admin")
    void shouldDeleteReplyAsAdmin() {
        // Arrange
        when(replyRepository.findById(reply.getId())).thenReturn(reply);
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(userRepository.findById(adminUser.getId())).thenReturn(adminUser);

        // Act
        useCase.execute(adminUser.getId(), reply.getId());

        // Assert
        assertTrue(reply.isDeleted());
        assertEquals(adminUser.getId(), reply.getUpdatedBy());
        verify(replyRepository).save(reply);
    }

    @Test
    @DisplayName("Should throw exception if reply already deleted")
    void shouldThrowIfAlreadyDeleted() {
        reply.markAsDeleted(replyAuthor.getId());
        when(replyRepository.findById(reply.getId())).thenReturn(reply);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.execute(replyAuthor.getId(), reply.getId()));

        assertEquals("Reply already deleted", ex.getMessage());
        verify(replyRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception if user not allowed to delete")
    void shouldThrowIfUserNotAllowed() {
        User otherUser = new User();
        otherUser.setId(999L);
        otherUser.setUsername("otherUser");

        when(replyRepository.findById(reply.getId())).thenReturn(reply);
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(userRepository.findById(otherUser.getId())).thenReturn(otherUser);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.execute(otherUser.getId(), reply.getId()));

        assertEquals("User not allowed to delete this reply", ex.getMessage());
        verify(replyRepository, never()).save(any());
    }

}