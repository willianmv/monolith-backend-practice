package com.simple.blog.backend.infra.config.usecase;

import com.simple.blog.backend.core.gateway.repository.IProfileRepository;
import com.simple.blog.backend.core.gateway.repository.IRefreshTokenRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.repository.IValidationCodeRepository;
import com.simple.blog.backend.core.gateway.service.*;
import com.simple.blog.backend.core.usecases.auth.activation.IActivateAccountUseCase;
import com.simple.blog.backend.core.usecases.auth.activation.ActivateAccountUseCase;
import com.simple.blog.backend.core.usecases.auth.login.ILoginUseCase;
import com.simple.blog.backend.core.usecases.auth.login.LoginUseCase;
import com.simple.blog.backend.core.usecases.auth.logout.ILogoutUseCase;
import com.simple.blog.backend.core.usecases.auth.logout.LogoutUseCase;
import com.simple.blog.backend.core.usecases.auth.refresh.IRefreshTokenUseCase;
import com.simple.blog.backend.core.usecases.auth.refresh.RefreshTokenUseCase;
import com.simple.blog.backend.core.usecases.auth.register.IRegisterUseCase;
import com.simple.blog.backend.core.usecases.auth.register.RegisterUseCase;
import com.simple.blog.backend.infra.gateway.service.LoggerService;
import com.simple.blog.backend.infra.gateway.service.SpringEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthUseCases {

    @Bean
    public IEventPublisher eventPublisherGateway(ApplicationEventPublisher springPublisher){
        return new SpringEventPublisher(springPublisher);
    }

    @Bean
    public IRegisterUseCase registerUseCase(
            IUserRepository userRepository, IProfileRepository profileRepository,
            IValidationCodeRepository validationCodeRepository, ICodeService codeService,
            IPasswordService passwordService, IEventPublisher eventPublisher) {

        ILoggerService loggerService = new LoggerService(RegisterUseCase.class);
        return new RegisterUseCase(
                userRepository, profileRepository,
                validationCodeRepository, codeService,
                passwordService, eventPublisher, loggerService);
    }

    @Bean
    public IActivateAccountUseCase activateAccountUseCase(
            IValidationCodeRepository validationCodeRepository,
            IUserRepository userRepository,
            IEventPublisher eventPublisher){

        ILoggerService loggerService = new LoggerService(ActivateAccountUseCase.class);
        return new ActivateAccountUseCase(
                validationCodeRepository,
                userRepository,
                eventPublisher, loggerService);
    }

    @Bean
    public ILoginUseCase loginUseCase(
            IJwtService jwtService,
            IUserRepository userRepository,
            IPasswordService passwordService,
            IRefreshTokenService refreshTokenService){

        ILoggerService loggerService = new LoggerService(LoginUseCase.class);
        return new LoginUseCase(
                jwtService,
                userRepository,
                passwordService,
                refreshTokenService, loggerService);
    }

    @Bean
    public IRefreshTokenUseCase refreshTokenUseCase(
            IJwtService jwtService,
            IUserRepository userRepository,
            IRefreshTokenRepository refreshTokenService
    ){
        ILoggerService loggerService = new LoggerService(RefreshTokenUseCase.class);
        return new RefreshTokenUseCase(
                jwtService,
                userRepository,
                refreshTokenService, loggerService);
    }

    @Bean
    public ILogoutUseCase logoutUseCase(
            IRefreshTokenService refreshTokenService,
            IUserRepository userRepository){

        ILoggerService loggerService = new LoggerService(LogoutUseCase.class);
        return new LogoutUseCase(refreshTokenService, userRepository, loggerService);
    }

}
