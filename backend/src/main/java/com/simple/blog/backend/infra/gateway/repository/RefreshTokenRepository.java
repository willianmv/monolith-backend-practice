package com.simple.blog.backend.infra.gateway.repository;

import com.simple.blog.backend.core.domain.RefreshToken;
import com.simple.blog.backend.core.gateway.repository.IRefreshTokenRepository;
import com.simple.blog.backend.infra.persistence.mapper.RefreshTokenMapper;
import com.simple.blog.backend.infra.persistence.repository.RefreshTokenEntityRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefreshTokenRepository implements IRefreshTokenRepository {

    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    public RefreshTokenRepository(RefreshTokenEntityRepository refreshTokenEntityRepository) {
        this.refreshTokenEntityRepository = refreshTokenEntityRepository;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return this.refreshTokenEntityRepository.findByToken(token)
                .map(RefreshTokenMapper::toCore);
    }
}
