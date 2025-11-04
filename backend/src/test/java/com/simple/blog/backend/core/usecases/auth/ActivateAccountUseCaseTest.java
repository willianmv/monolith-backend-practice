package com.simple.blog.backend.core.usecases.auth;

import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.domain.ValidationCode;
import com.simple.blog.backend.core.event.ActivatedAccountEvent;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.repository.IValidationCodeRepository;
import com.simple.blog.backend.core.gateway.service.IEventPublisher;
import com.simple.blog.backend.core.usecases.auth.activation.ActivateAccountUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Activate Account Use Case")
class ActivateAccountUseCaseTest {

    @Mock
    private IValidationCodeRepository validationCodeRepositoryGateway;

    @Mock
    private IUserRepository userRepositoryGateway;

    @Mock
    private IEventPublisher eventPublisherGateway;

    @InjectMocks
    private ActivateAccountUseCase useCase;

    @Test
    @DisplayName("Should activate account successfully")
    void shouldActivateAccountSuccessfully() {
        // Arrange
        User user = new User();
        user.setActive(false);

        ValidationCode code = mock(ValidationCode.class);
        when(code.isExpired()).thenReturn(false);
        when(code.isValidated()).thenReturn(false);
        when(code.getUser()).thenReturn(user);
        when(validationCodeRepositoryGateway.findByCode("valid-code")).thenReturn(code);

        // Act
        useCase.execute("valid-code");

        // Assert
        verify(code).markAsValidated();
        verify(validationCodeRepositoryGateway).save(code);
        verify(userRepositoryGateway).save(user);
        verify(eventPublisherGateway).publish(any(ActivatedAccountEvent.class));
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("Should throw when code is expired")
    void shouldThrowWhenCodeIsExpired() {
        // Arrange
        ValidationCode code = mock(ValidationCode.class);
        when(code.isExpired()).thenReturn(true);

        when(validationCodeRepositoryGateway.findByCode("expired-code")).thenReturn(code);

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute("expired-code"));

        assertEquals("Code expired", ex.getMessage());
        verify(code, never()).markAsValidated();
        verify(userRepositoryGateway, never()).save(any());
        verify(eventPublisherGateway, never()).publish(any());
    }

    @Test
    @DisplayName("Should throw when code is already validated")
    void shouldThrowWhenCodeIsAlreadyValidated() {
        // Arrange
        ValidationCode code = mock(ValidationCode.class);
        when(code.isExpired()).thenReturn(false);
        when(code.isValidated()).thenReturn(true);

        when(validationCodeRepositoryGateway.findByCode("used-code")).thenReturn(code);

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute("used-code"));

        assertEquals("Code already used", ex.getMessage());
        verify(code, never()).markAsValidated();
        verify(userRepositoryGateway, never()).save(any());
        verify(eventPublisherGateway, never()).publish(any());
    }


}