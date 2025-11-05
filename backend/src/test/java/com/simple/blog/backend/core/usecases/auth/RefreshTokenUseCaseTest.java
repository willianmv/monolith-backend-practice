package com.simple.blog.backend.core.usecases.auth;

import com.simple.blog.backend.core.domain.RefreshToken;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.repository.IRefreshTokenRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.IJwtService;
import com.simple.blog.backend.core.gateway.service.ILoggerService;
import com.simple.blog.backend.core.usecases.auth.login.LoginOutput;
import com.simple.blog.backend.core.usecases.auth.refresh.RefreshTokenUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Refresh Token Use Case Test")
class RefreshTokenUseCaseTest {

    @Mock
    private IJwtService jwtServiceGateway;

    @Mock
    private IUserRepository userRepositoryGateway;

    @Mock
    private IRefreshTokenRepository refreshTokenRepositoryGateway;

    @Mock
    private ILoggerService loggerService;

    @InjectMocks
    private RefreshTokenUseCase useCase;


    @Test
    @DisplayName("Should refresh access token successfully")
    void shouldRefreshAccessTokenSuccessfully() {
        // Arrange
        String token = "refresh-token-uuid";
        long userId = 1L;

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserId(userId);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(3600)); // válido

        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setUsername("user123");

        when(refreshTokenRepositoryGateway.findByToken(token)).thenReturn(Optional.of(refreshToken));
        when(userRepositoryGateway.findById(userId)).thenReturn(user);
        when(jwtServiceGateway.generateToken(eq(user.getEmail()), anyMap())).thenReturn("new-access-token");

        // Act
        LoginOutput output = useCase.execute(token);

        // Assert
        assertEquals("new-access-token", output.accessToken());
        assertEquals(token, output.refreshToken());
        verify(jwtServiceGateway).generateToken(user.getEmail(),
                Map.of("id", user.getId(), "username", user.getUsername()));
    }

    @Test
    @DisplayName("Should throw when refresh token not found")
    void shouldThrowWhenRefreshTokenNotFound() {
        // Arrange
        String token = "invalid-token";
        when(refreshTokenRepositoryGateway.findByToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute(token));

        assertEquals("Invalid refresh token: invalid-token", ex.getMessage());
        verify(loggerService).warn(contains("not found"));
    }

    @Test
    @DisplayName("Should throw when token is expired")
    void shouldThrowWhenTokenIsExpired() {
        // Arrange
        String token = "expired-token";

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserId(1L);
        refreshToken.setExpiresAt(Instant.now().minusSeconds(1)); // expirado

        when(refreshTokenRepositoryGateway.findByToken(token)).thenReturn(Optional.of(refreshToken));

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute(token));

        assertEquals("Refresh token is invalid or expired", ex.getMessage());
        verify(loggerService).warn(contains("expired"));
    }

    @Test
    @DisplayName("Should reuse same refresh token if valid")
    void shouldReuseSameRefreshTokenIfValid() {
        // Arrange
        String token = "existing-refresh-token";
        long userId = 1L;

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserId(userId);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(600)); // ainda válido

        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setUsername("user123");

        when(refreshTokenRepositoryGateway.findByToken(token)).thenReturn(Optional.of(refreshToken));
        when(userRepositoryGateway.findById(userId)).thenReturn(user);
        when(jwtServiceGateway.generateToken(eq(user.getEmail()), anyMap())).thenReturn("access-token");

        // Act
        LoginOutput output = useCase.execute(token);

        // Assert
        assertEquals("access-token", output.accessToken());
        assertEquals(token, output.refreshToken());
        verify(loggerService).info(contains("New access token generated"));
    }
}