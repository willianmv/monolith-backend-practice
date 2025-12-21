package com.simple.blog.backend.core.usecases.auth.activation;

import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.domain.ValidationCode;
import com.simple.blog.backend.core.event.ActivatedAccountEvent;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.IValidationCodeRepository;
import com.simple.blog.backend.core.gateway.IEventPublisher;
import com.simple.blog.backend.core.gateway.ILoggerService;

public class ActivateAccountUseCase implements IActivateAccountUseCase {

    private final IValidationCodeRepository validationCodeRepositoryGateway;
    private final IUserRepository userRepositoryGateway;
    private final IEventPublisher eventPublisherGateway;
    private final ILoggerService loggerService;

    public ActivateAccountUseCase(IValidationCodeRepository validationCodeRepositoryGateway, IUserRepository userRepositoryGateway, IEventPublisher eventPublisherGateway, ILoggerService loggerService) {
        this.validationCodeRepositoryGateway = validationCodeRepositoryGateway;
        this.userRepositoryGateway = userRepositoryGateway;
        this.eventPublisherGateway = eventPublisherGateway;
        this.loggerService = loggerService;
    }

    @Override
    public void execute(String code) {
        loggerService.info("[ACTIVATE] Starting account activation for code: " + code);

        ValidationCode validationCode = validationCodeRepositoryGateway.findByCode(code);

        if(validationCode.isExpired()) {
            loggerService.warn("[ACTIVATE] Activation code is expired");
            throw new DomainException("Code expired");
        }
        if (validationCode.isValidated()) {
            loggerService.warn("[ACTIVATE] Activation code has already been used");
            throw new DomainException("Code already used");
        }

        User user = validationCode.getUser();
        user.setActive(true);
        validationCode.markAsValidated();

        validationCodeRepositoryGateway.save(validationCode);
        userRepositoryGateway.save(user);
        loggerService.info("[ACTIVATE] User account marked as active: " + user.getEmail());

        eventPublisherGateway.publish(new ActivatedAccountEvent(user));
        loggerService.info("[ACTIVATE] Account activated event published for: " + user.getEmail());
    }
}
