package com.simple.blog.backend.core.usecases.auth.login;

import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.IJwtService;
import com.simple.blog.backend.core.gateway.service.ILoggerService;
import com.simple.blog.backend.core.gateway.service.IPasswordService;
import com.simple.blog.backend.core.gateway.service.IRefreshTokenService;

import java.util.Map;

public class LoginUseCase implements ILoginUseCase {

    private final IJwtService jwtServiceGateway;
    private final IUserRepository userRepositoryGateway;
    private final IPasswordService passwordServiceGateway;
    private final IRefreshTokenService refreshTokenServiceGateway;
    private final ILoggerService loggerService;

    public LoginUseCase(IJwtService jwtServiceGateway, IUserRepository userRepositoryGateway, IPasswordService passwordServiceGateway, IRefreshTokenService refreshTokenServiceGateway, ILoggerService loggerService) {
        this.jwtServiceGateway = jwtServiceGateway;
        this.userRepositoryGateway = userRepositoryGateway;
        this.passwordServiceGateway = passwordServiceGateway;
        this.refreshTokenServiceGateway = refreshTokenServiceGateway;
        this.loggerService = loggerService;
    }

    @Override
    public LoginOutput execute(LoginInput loginInput) {
        loggerService.info("[LOGIN] Attempting login for email: " + loginInput.email());

        User user = userRepositoryGateway.findByEmail(loginInput.email())
                .orElseThrow(() -> {
                    loggerService.warn("[LOGIN] No user found for email: " + loginInput.email());
                    return new DomainException("User not found by e-mail: " + loginInput.email());
                });

        if(!user.isActive()){
            loggerService.warn("[LOGIN] Inactive account for email: " + loginInput.email());
            throw new DomainException("User account not active yet");
        }

        if(!passwordServiceGateway.matches(loginInput.password(), user.getPassword())){
            loggerService.warn("[LOGIN] Invalid password for email: " + loginInput.email());
            throw new DomainException("Invalid password");
        }

        loggerService.info("[LOGIN] Credentials validated. Generating tokens...");

        String token = jwtServiceGateway.generateToken(user.getEmail(),
                Map.of("id", user.getId(), "username", user.getUsername()));

        String refreshToken = refreshTokenServiceGateway.generateToken(user.getId());

        loggerService.info("[LOGIN] Tokens generated successfully for user: " + user.getEmail());
        return new LoginOutput(token, refreshToken);
    }
}
