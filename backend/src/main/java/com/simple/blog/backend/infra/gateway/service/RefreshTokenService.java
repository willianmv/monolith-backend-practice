package com.simple.blog.backend.infra.gateway.service;

import com.simple.blog.backend.core.domain.RefreshToken;
import com.simple.blog.backend.core.gateway.service.IRefreshTokenService;
import com.simple.blog.backend.infra.persistence.mapper.RefreshTokenMapper;
import com.simple.blog.backend.infra.persistence.entity.RefreshTokenEntity;
import com.simple.blog.backend.infra.persistence.repository.RefreshTokenEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {

    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    public RefreshTokenService(RefreshTokenEntityRepository refreshTokenEntityRepository) {
        this.refreshTokenEntityRepository = refreshTokenEntityRepository;
    }

    @Override
    @Transactional
    public String generateToken(long userId) {
        Optional<RefreshTokenEntity> existingToken = this.refreshTokenEntityRepository.findByUserId(userId);
        existingToken.ifPresent(entity ->{
                    this.refreshTokenEntityRepository.deleteByUserId(entity.getUserId());
                    refreshTokenEntityRepository.flush();
                });

        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.create(userId, token);
        this.refreshTokenEntityRepository.save(RefreshTokenMapper.toJpaEntity(refreshToken));
        return token;
    }


    @Override
    @Transactional
    public boolean revokeToken(long userId, String refreshToken) {
        Optional<RefreshTokenEntity> existingToken = this.refreshTokenEntityRepository.findByUserId(userId);

        if(existingToken.isPresent() && existingToken.get().getToken().equals(refreshToken)){
            this.refreshTokenEntityRepository.deleteByUserId(userId);
            return true;
        }

        return false;
    }
}
