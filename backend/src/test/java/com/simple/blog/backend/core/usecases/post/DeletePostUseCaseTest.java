package com.simple.blog.backend.core.usecases.post;

import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.Profile;
import com.simple.blog.backend.core.domain.RoleType;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.ILoggerService;
import com.simple.blog.backend.core.usecases.post.delete.DeletePostUseCase;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("DeletePostUseCase Test")
class DeletePostUseCaseTest {


    @Mock
    private IPostRepository postRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ILoggerService loggerService;

    @InjectMocks
    private DeletePostUseCase useCase;

    private User owner;
    private User admin;
    private User otherUser;
    private Post post;

    @BeforeEach
    void setup() {
        owner = new User();
        owner.setId(1L);

        admin = new User();
        admin.setId(2L);
        Profile adminProfile = new Profile(RoleType.ADMIN, RoleType.ADMIN.getDescription());
        admin.setProfiles(Set.of(adminProfile));

        otherUser = new User();
        otherUser.setId(3L);

        post = Post.create("Title", "Content", "image.png", owner, null);
        post.setId(100L);
        post.setDeleted(null);
        post.setCreatedAt(Instant.now());
    }

    @Test
    @DisplayName("Should delete post successfully if user is owner")
    void shouldDeletePostIfOwner() {
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(userRepository.findById(owner.getId())).thenReturn(owner);

        useCase.execute(post.getId(), owner.getId());

        assertTrue(post.isDeleted());
        assertEquals(owner.getId(), post.getUpdatedBy());
        verify(postRepository).save(post);
    }

    @Test
    @DisplayName("Should delete post successfully if user is admin")
    void shouldDeletePostIfAdmin() {
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(userRepository.findById(admin.getId())).thenReturn(admin);

        useCase.execute(post.getId(), admin.getId());

        assertTrue(post.isDeleted());
        assertEquals(admin.getId(), post.getUpdatedBy());
        verify(postRepository).save(post);
    }

    @Test
    @DisplayName("Should throw exception if post already deleted")
    void shouldThrowIfPostAlreadyDeleted() {
        post.setDeleted(Instant.now());
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(userRepository.findById(owner.getId())).thenReturn(owner);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.execute(post.getId(), owner.getId()));

        assertEquals("Post already deleted", ex.getMessage());
        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception if user is neither owner nor admin")
    void shouldThrowIfUserNotAllowed() {
        when(postRepository.findById(post.getId())).thenReturn(post);
        when(userRepository.findById(otherUser.getId())).thenReturn(otherUser);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.execute(post.getId(), otherUser.getId()));

        assertEquals("User not allowed to delete this post", ex.getMessage());
        verify(postRepository, never()).save(any());
    }

}