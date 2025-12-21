package com.simple.blog.backend.core.usecases.auth.refresh;

import com.simple.blog.backend.core.domain.RefreshToken;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.IRefreshTokenRepository;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.IJwtService;
import com.simple.blog.backend.core.gateway.ILoggerService;
import com.simple.blog.backend.core.usecases.auth.login.LoginOutput;

import java.time.Instant;
import java.util.Map;

public class RefreshTokenUseCase implements IRefreshTokenUseCase {

    private final IJwtService jwtServiceGateway;
    private final IUserRepository userRepositoryGateway;
    private final IRefreshTokenRepository refreshTokenRepositoryGateway;
    private final ILoggerService loggerService;

    public RefreshTokenUseCase(IJwtService jwtServiceGateway, IUserRepository userRepositoryGateway, IRefreshTokenRepository refreshTokenRepositoryGateway, ILoggerService loggerService) {
        this.jwtServiceGateway = jwtServiceGateway;
        this.userRepositoryGateway = userRepositoryGateway;
        this.refreshTokenRepositoryGateway = refreshTokenRepositoryGateway;
        this.loggerService = loggerService;
    }

    @Override
    public LoginOutput execute(String token) {
        loggerService.info("[REFRESH] Attempting to refresh access token using refresh token: " + token);

        RefreshToken refreshToken = this.refreshTokenRepositoryGateway.findByToken(token)
                .orElseThrow(() -> {
                    loggerService.warn("[REFRESH] Refresh token not found: " + token);
                    return new DomainException("Invalid refresh token: " + token);
                });

        if(refreshToken.getExpiresAt().isBefore(Instant.now())){
            loggerService.warn("[REFRESH] Refresh token expired at: " + refreshToken.getExpiresAt());
            throw new DomainException("Refresh token is invalid or expired");
        }

        User user = userRepositoryGateway.findById(refreshToken.getUserId());
        loggerService.info("[REFRESH] Valid refresh token for user: " + user.getEmail());

        String newAccessToken = jwtServiceGateway.generateToken(user.getEmail(),
                Map.of("id", user.getId(), "username", user.getUsername()));

        loggerService.info("[REFRESH] New access token generated for user: " + user.getEmail());
        return new LoginOutput(newAccessToken, refreshToken.getToken());
    }
}
