package com.simple.blog.backend.infra.persistence.mapper;

import com.simple.blog.backend.core.domain.RefreshToken;
import com.simple.blog.backend.infra.persistence.entity.RefreshTokenEntity;

public class RefreshTokenMapper {

    public static RefreshToken toCore(RefreshTokenEntity entity){
        RefreshToken core = new RefreshToken();
        core.setId(entity.getId());
        core.setUserId(entity.getUserId());
        core.setToken(entity.getToken());
        core.setCreatedAt(entity.getCreatedAt());
        core.setExpiresAt(entity.getExpiresAt());
        return core;
    }

    public static RefreshTokenEntity toJpaEntity(RefreshToken core){
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(core.getId());
        entity.setUserId(core.getUserId());
        entity.setToken(core.getToken());
        entity.setCreatedAt(core.getCreatedAt());
        entity.setExpiresAt(core.getExpiresAt());
        return entity;
    }

}
