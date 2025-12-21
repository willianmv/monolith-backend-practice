package com.simple.blog.backend.core.gateway;

import com.simple.blog.backend.core.domain.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepository {

    Optional<RefreshToken> findByToken(String token);

}
