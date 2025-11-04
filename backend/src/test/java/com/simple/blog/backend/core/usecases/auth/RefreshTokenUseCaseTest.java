package com.simple.blog.backend.core.usecases.auth;

import com.simple.blog.backend.core.domain.RefreshToken;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.repository.IRefreshTokenRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.IJwtService;
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
        refreshToken.setExpiresAt(Instant.now().plusSeconds(3600));
        refreshToken.setRevoked(false);

        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setUsername("user123");

        when(refreshTokenRepositoryGateway.findByToken(token)).thenReturn(Optional.of(refreshToken));
        when(userRepositoryGateway.findById(userId)).thenReturn(user);
        when(jwtServiceGateway.generateToken(user.getEmail(),
                Map.of("id", user.getId(), "username", user.getUsername())))
                .thenReturn("new-access-token");

        // Act
        LoginOutput output = useCase.execute(token);

        // Assert
        assertEquals("new-access-token", output.accessToken());
        assertEquals(token, output.refreshToken());
    }

    @Test
    @DisplayName("Should throw if refresh token not found")
    void shouldThrowIfRefreshTokenNotFound() {
        // Arrange
        String token = "invalid-token";
        when(refreshTokenRepositoryGateway.findByToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute(token));
        assertEquals("Invalid refresh token: invalid-token", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw if token is expired")
    void shouldThrowIfTokenIsExpired() {
        // Arrange
        String token = "expired-token";

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setExpiresAt(Instant.now().minusSeconds(1)); // já expirado
        refreshToken.setRevoked(false);

        when(refreshTokenRepositoryGateway.findByToken(token)).thenReturn(Optional.of(refreshToken));

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute(token));
        assertEquals("Refresh token is invalid or expired", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw if token is revoked")
    void shouldThrowIfTokenIsRevoked() {
        // Arrange
        String token = "revoked-token";

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(3600));
        refreshToken.setRevoked(true);

        when(refreshTokenRepositoryGateway.findByToken(token)).thenReturn(Optional.of(refreshToken));

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute(token));
        assertEquals("Refresh token is invalid or expired", ex.getMessage());
    }

    @Test
    @DisplayName("Should reuse the same refresh token")
    void shouldReuseSameRefreshToken() {
        // Arrange
        String token = "existing-refresh-token";
        long userId = 1L;

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserId(userId);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(600));
        refreshToken.setRevoked(false);

        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setUsername("user123");

        when(refreshTokenRepositoryGateway.findByToken(token)).thenReturn(Optional.of(refreshToken));
        when(userRepositoryGateway.findById(userId)).thenReturn(user);
        when(jwtServiceGateway.generateToken(user.getEmail(), Map.of("id", user.getId(), "username", user.getUsername())))
                .thenReturn("access-token");

        // Act
        LoginOutput output = useCase.execute(token);

        // Assert
        assertEquals("access-token", output.accessToken());
        assertEquals(token, output.refreshToken()); // mesmo token reutilizado
    }

}