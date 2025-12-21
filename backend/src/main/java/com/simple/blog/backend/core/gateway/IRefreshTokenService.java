package com.simple.blog.backend.core.gateway;

public interface IRefreshTokenService {

    String generateToken(long userId);

    boolean revokeToken(long userId, String refreshToken);

}
