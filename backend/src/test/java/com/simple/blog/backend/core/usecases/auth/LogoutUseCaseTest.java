package com.simple.blog.backend.core.usecases.auth;

import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.ILoggerService;
import com.simple.blog.backend.core.gateway.IRefreshTokenService;
import com.simple.blog.backend.core.usecases.auth.logout.LogoutUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Logout Use Case")
class LogoutUseCaseTest {

    @Mock
    private IRefreshTokenService refreshTokenServiceGateway;

    @Mock
    private IUserRepository userRepositoryGateway;

    @Mock
    private ILoggerService loggerService;

    @InjectMocks
    private LogoutUseCase useCase;


    @Test
    @DisplayName("Should logout successfully and revoke refresh token")
    void shouldLogoutSuccessfully() {
        // Arrange
        long userId = 1L;
        String refreshToken = "valid-refresh-token";

        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");

        when(userRepositoryGateway.findById(userId)).thenReturn(user);
        when(refreshTokenServiceGateway.revokeToken(userId, refreshToken)).thenReturn(true);

        // Act
        useCase.execute(userId, refreshToken);

        // Assert
        verify(refreshTokenServiceGateway).revokeToken(userId, refreshToken);
        verify(loggerService).info(contains("Refresh token revoked successfully"));
    }


    @Test
    @DisplayName("Should throw if user not found")
    void shouldThrowIfUserNotFound() {
        // Arrange
        long userId = 99L;
        String refreshToken = "token";
        when(userRepositoryGateway.findById(userId)).thenThrow(new DomainException("User not found"));

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute(userId, refreshToken));

        assertEquals("User not found", ex.getMessage());
        verify(loggerService, never()).info(contains("Refresh token revoked"));
    }

    @Test
    @DisplayName("Should throw if refresh token revocation fails")
    void shouldThrowIfRefreshTokenRevocationFails() {
        // Arrange
        long userId = 1L;
        String refreshToken = "invalid-token";

        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");

        when(userRepositoryGateway.findById(userId)).thenReturn(user);
        when(refreshTokenServiceGateway.revokeToken(userId, refreshToken)).thenReturn(false);

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute(userId, refreshToken));

        assertEquals("Failed to revoke refresh token or token not found", ex.getMessage());
        verify(loggerService).warn(contains("Failed to revoke refresh token"));
    }


}