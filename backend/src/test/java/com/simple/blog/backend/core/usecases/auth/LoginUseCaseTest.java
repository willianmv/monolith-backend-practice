package com.simple.blog.backend.core.usecases.auth;

import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.IJwtService;
import com.simple.blog.backend.core.gateway.service.IPasswordService;
import com.simple.blog.backend.core.gateway.service.IRefreshTokenService;
import com.simple.blog.backend.core.usecases.auth.login.LoginInput;
import com.simple.blog.backend.core.usecases.auth.login.LoginOutput;
import com.simple.blog.backend.core.usecases.auth.login.LoginUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Login Use Case")
class LoginUseCaseTest {

    @Mock
    private IJwtService jwtServiceGateway;

    @Mock
    private IUserRepository userRepositoryGateway;

    @Mock
    private IPasswordService passwordServiceGateway;

    @Mock
    private IRefreshTokenService refreshTokenServiceGateway;

    @InjectMocks
    private LoginUseCase useCase;

    @Test
    @DisplayName("Should login successfully")
    void shouldLoginSuccessfully() {
        // Arrange
        String email = "user@example.com";
        String password = "123456";

        User user = new User();
        user.setEmail(email);
        user.setPassword("hashed");
        user.setId(1L);
        user.setUsername("user123");
        user.setActive(true);

        when(userRepositoryGateway.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordServiceGateway.matches(password, "hashed")).thenReturn(true);
        when(jwtServiceGateway.generateToken(eq(email), anyMap())).thenReturn("jwt-token");
        when(refreshTokenServiceGateway.generateToken(user.getId())).thenReturn("refresh-token-uuid");

        // Act
        LoginInput input = new LoginInput(email, password);
        LoginOutput output = useCase.execute(input);

        // Assert
        assertEquals("jwt-token", output.accessToken());
        assertEquals("refresh-token-uuid", output.refreshToken());
    }

    @Test
    @DisplayName("Should throw if user not found")
    void shouldThrowIfUserNotFound() {
        // Arrange
        when(userRepositoryGateway.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () ->
                useCase.execute(new LoginInput("notfound@example.com", "123456")));

        assertEquals("User not found by e-mail: notfound@example.com", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw if user not active")
    void shouldThrowIfUserIsNotActive() {
        // Arrange
        User user = new User();
        user.setActive(false);
        when(userRepositoryGateway.findByEmail("inactive@example.com")).thenReturn(Optional.of(user));

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () ->
                useCase.execute(new LoginInput("inactive@example.com", "123456")));

        assertEquals("User account not active yet", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw if password is invalid")
    void shouldThrowIfPasswordIsInvalid() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("hashed");
        user.setActive(true);

        when(userRepositoryGateway.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordServiceGateway.matches("wrong", "hashed")).thenReturn(false);

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () ->
                useCase.execute(new LoginInput("user@example.com", "wrong")));

        assertEquals("Invalid password", ex.getMessage());
    }

}