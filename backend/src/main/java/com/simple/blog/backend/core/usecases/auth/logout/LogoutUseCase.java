package com.simple.blog.backend.core.usecases.auth.logout;

import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.ILoggerService;
import com.simple.blog.backend.core.gateway.service.IRefreshTokenService;

public class LogoutUseCase implements ILogoutUseCase {

    private final IRefreshTokenService refreshTokenServiceGateway;
    private final IUserRepository userRepositoryGateway;
    private final ILoggerService loggerService;

    public LogoutUseCase(IRefreshTokenService refreshTokenServiceGateway, IUserRepository userRepositoryGateway, ILoggerService loggerService) {
        this.refreshTokenServiceGateway = refreshTokenServiceGateway;
        this.userRepositoryGateway = userRepositoryGateway;
        this.loggerService = loggerService;
    }

    @Override
    public void execute(long userId, String refreshToken) {
        loggerService.info("[LOGOUT] Attempting logout for user ID: " + userId);

        User existingUser = this.userRepositoryGateway.findById(userId);
        loggerService.info("[LOGOUT] Found user: " + existingUser.getEmail());

        boolean success = this.refreshTokenServiceGateway.revokeToken(existingUser.getId(), refreshToken);
        if(!success){
            loggerService.warn("[LOGOUT] Failed to revoke refresh token for user ID: " + userId);
            throw new DomainException("Failed to revoke refresh token or token not found");
        }

        loggerService.info("[LOGOUT] Refresh token revoked successfully for user: " + existingUser.getEmail());
    }
}
