package com.simple.blog.backend.core.usecases.auth;

import com.simple.blog.backend.core.domain.Profile;
import com.simple.blog.backend.core.domain.RoleType;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.domain.ValidationCode;
import com.simple.blog.backend.core.event.AccountActivationEvent;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.repository.IProfileRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.repository.IValidationCodeRepository;
import com.simple.blog.backend.core.gateway.service.ICodeService;
import com.simple.blog.backend.core.gateway.service.IEventPublisher;
import com.simple.blog.backend.core.gateway.service.IPasswordService;
import com.simple.blog.backend.core.usecases.auth.register.RegisterInput;
import com.simple.blog.backend.core.usecases.auth.register.RegisterUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Register Use Cases")
class RegisterUseCaseTest {

    @Mock
    IUserRepository userRepositoryGateway;

    @Mock
    IProfileRepository profileRepositoryGateway;

    @Mock
    IValidationCodeRepository validationCodeRepositoryGateway;

    @Mock
    ICodeService codeServiceGateway;

    @Mock
    IPasswordService passwordServiceGateway;

    @Mock
    IEventPublisher eventPublisherGateway;

    @InjectMocks
    RegisterUseCase registerUseCase;

    @Test
    @DisplayName("Should register new user successfully")
    void shouldRegisterNewUserSuccessfully() {
        // Arrange
        String email = "test@email.com";
        String username = "john123";
        String rawPassword = "pass";
        String encodedPassword = "encodedPass";
        String generatedCode = "123456";

        RegisterInput input = new RegisterInput("John", username, email, rawPassword);

        when(userRepositoryGateway.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepositoryGateway.existsByUsername(username)).thenReturn(false);
        when(passwordServiceGateway.encode(rawPassword)).thenReturn(encodedPassword);

        Profile regularProfile = mock(Profile.class);
        when(profileRepositoryGateway.findByRole(RoleType.REGULAR)).thenReturn(regularProfile);

        when(codeServiceGateway.generate()).thenReturn(generatedCode);

        doNothing().when(validationCodeRepositoryGateway).save(any());
        doNothing().when(eventPublisherGateway).publish(any());

        // Act
        registerUseCase.execute(input);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<ValidationCode> codeCaptor = ArgumentCaptor.forClass(ValidationCode.class);
        ArgumentCaptor<AccountActivationEvent> eventCaptor = ArgumentCaptor.forClass(AccountActivationEvent.class);

        verify(userRepositoryGateway).save(userCaptor.capture());
        verify(validationCodeRepositoryGateway).save(codeCaptor.capture());
        verify(eventPublisherGateway).publish(eventCaptor.capture());

        User savedUser = userCaptor.getValue();
        ValidationCode savedCode = codeCaptor.getValue();
        AccountActivationEvent event = eventCaptor.getValue();

        assertEquals(email, savedUser.getEmail());
        assertEquals(username, savedUser.getUsername());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertTrue(savedUser.getProfiles().contains(regularProfile));

        assertEquals(savedUser, savedCode.getUser());
        assertEquals(generatedCode, savedCode.getCode());

        assertEquals(savedUser, event.user());
        assertEquals(generatedCode, event.code());
        assertEquals(savedCode.getExpiresAt(), event.expiresAt());
    }



    @Test
    @DisplayName("Should throw when user exists and is active")
    void shouldThrowWhenUserExistsAndIsActive(){
        //Arrange
        User activeUser = mock(User.class);
        when(activeUser.isActive()).thenReturn(true);
        when(userRepositoryGateway.findByEmail("test@email.com")).thenReturn(Optional.of(activeUser));

        RegisterInput input = new RegisterInput("John", "john123", "test@email.com", "pass");

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> registerUseCase.execute(input));
        assertEquals("E-mail or username already in use and already activated", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw when user exists with valid code")
    void shouldThrowWhenUserExistsWithValidCode() {
        // Arrange
        User user = mock(User.class);
        when(user.isActive()).thenReturn(false);
        when(user.getId()).thenReturn(1L);

        ValidationCode validCode = mock(ValidationCode.class);
        when(validCode.isExpired()).thenReturn(false);

        when(userRepositoryGateway.findByEmail("test@email.com")).thenReturn(Optional.of(user));
        when(validationCodeRepositoryGateway.findLatestByUserId(1L)).thenReturn(validCode);

        RegisterInput input = new RegisterInput("John", "john123", "test@email.com", "pass");

        // Act && Assert
        DomainException ex = assertThrows(DomainException.class, () -> registerUseCase.execute(input));
        assertEquals("A code has already been sent and is still valid. Please check your email", ex.getMessage());
    }

    @Test
    @DisplayName("Should resend code when user exists and code is expired")
    void shouldResendCodeWhenUserExistsAndCodeExpired() {
        // Arrange
        User user = mock(User.class);
        when(user.isActive()).thenReturn(false);
        when(user.getId()).thenReturn(1L);

        ValidationCode expiredCode = mock(ValidationCode.class);
        when(expiredCode.isExpired()).thenReturn(true);

        when(userRepositoryGateway.findByEmail("test@email.com")).thenReturn(Optional.of(user));
        when(validationCodeRepositoryGateway.findLatestByUserId(1L)).thenReturn(expiredCode);

        when(codeServiceGateway.generate()).thenReturn("123456");
        doNothing().when(validationCodeRepositoryGateway).save(any(ValidationCode.class));

        RegisterInput input = new RegisterInput("John", "john123", "test@email.com", "pass");

        // Act
        registerUseCase.execute(input);

        // Assert
        verify(validationCodeRepositoryGateway).save(any(ValidationCode.class));
        verify(eventPublisherGateway).publish(any(AccountActivationEvent.class));
    }

    @Test
    @DisplayName("Should throw when username is already in use")
    void shouldThrowWhenUsernameAlreadyInUse() {
        // Arrange
        when(userRepositoryGateway.findByEmail("test@email.com")).thenReturn(Optional.empty());
        when(userRepositoryGateway.existsByUsername("john123")).thenReturn(true);

        RegisterInput input = new RegisterInput("John", "john123", "test@email.com", "pass");

        // Act && Assert
        DomainException ex = assertThrows(DomainException.class, () -> registerUseCase.execute(input));
        assertEquals("Username already in use", ex.getMessage());
    }

}