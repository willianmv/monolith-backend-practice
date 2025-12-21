package com.simple.blog.backend.core.usecases.auth.register;

import com.simple.blog.backend.core.domain.Profile;
import com.simple.blog.backend.core.domain.RoleType;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.domain.ValidationCode;
import com.simple.blog.backend.core.event.AccountActivationEvent;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.IProfileRepository;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.IValidationCodeRepository;
import com.simple.blog.backend.core.gateway.ICodeService;
import com.simple.blog.backend.core.gateway.IEventPublisher;
import com.simple.blog.backend.core.gateway.ILoggerService;
import com.simple.blog.backend.core.gateway.IPasswordService;

import java.util.Optional;
import java.util.Set;

public class RegisterUseCase implements IRegisterUseCase {

    private final IUserRepository userRepository;
    private final IProfileRepository profileRepository;
    private final IValidationCodeRepository validationCodeRepository;
    private final ICodeService codeService;
    private final IPasswordService passwordService;
    private final IEventPublisher eventPublisher;
    private final ILoggerService loggerService;

    public RegisterUseCase(IUserRepository userRepository, IProfileRepository profileRepository, IValidationCodeRepository validationCodeRepository, ICodeService codeService, IPasswordService passwordService, IEventPublisher eventPublisher, ILoggerService loggerService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.validationCodeRepository = validationCodeRepository;
        this.codeService = codeService;
        this.passwordService = passwordService;
        this.eventPublisher = eventPublisher;
        this.loggerService = loggerService;
    }

    @Override
    public void execute(RegisterInput registerInput) {
        loggerService.info("[REGISTER] Starting registration process for email: "+ registerInput.email());

        Optional<User> existingUserOpt = userRepository.findByEmail(registerInput.email());

        if(existingUserOpt.isPresent()){
            User existingUser = existingUserOpt.get();
            loggerService.info("[REGISTER] Existing user found with ID: " + existingUser.getId());

            if(existingUser.isActive()){
                loggerService.warn("[REGISTER] Attempt to register with already active email: " + registerInput.email());
                throw new DomainException("E-mail or username already in use and already activated");
            }

            ValidationCode existingCode = validationCodeRepository.findLatestByUserId(existingUser.getId());

            if(existingCode != null && !existingCode.isExpired()){
                loggerService.warn("[REGISTER] Validation code still valid for user ID: " + existingUser.getId());
                throw new DomainException("A code has already been sent and is still valid. Please check your email");
            }

            String newCode = codeService.generate();
            loggerService.info("[REGISTER] Generating new validation code for user ID: " + existingUser.getId());

            ValidationCode newValidationCode = ValidationCode.createForUser(newCode, existingUser);
            validationCodeRepository.save(newValidationCode);

            eventPublisher.publish(new AccountActivationEvent(existingUser, newCode, newValidationCode.getExpiresAt()));
            loggerService.info("[REGISTER] Account activation event published for user: " + existingUser.getEmail());
            return;
        }

        if(userRepository.existsByUsername(registerInput.username())){
            loggerService.warn("[REGISTER] Username already in use: " + registerInput.username());
            throw new DomainException("Username already in use");
        }

        String encodedPassword = passwordService.encode(registerInput.rawPassword());
        User newUser = User.createBasic(registerInput.name(), registerInput.username(), registerInput.email(), encodedPassword);
        Profile regularRole = profileRepository.findByRole(RoleType.REGULAR);
        newUser.setProfiles(Set.of(regularRole));
        userRepository.save(newUser);

        String code = codeService.generate();
        ValidationCode validationCode = ValidationCode.createForUser(code, newUser);
        validationCodeRepository.save(validationCode);
        loggerService.info("[REGISTER] Activation code generated and saved, expires at: " + validationCode.getExpiresAt());

        eventPublisher.publish(new AccountActivationEvent(newUser, code, validationCode.getExpiresAt()));
        loggerService.info("[REGISTER] Account activation event published for new user");
    }
}
