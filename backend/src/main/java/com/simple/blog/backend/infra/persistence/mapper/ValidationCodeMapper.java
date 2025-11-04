package com.simple.blog.backend.infra.persistence.mapper;

import com.simple.blog.backend.core.domain.ValidationCode;
import com.simple.blog.backend.infra.persistence.entity.ValidationCodeEntity;

public class ValidationCodeMapper {

    public static ValidationCode toCore(ValidationCodeEntity entity, boolean full){
        if(entity == null) return null;

        ValidationCode validationCode = new ValidationCode();
        validationCode.setId(entity.getId());
        validationCode.setCode(entity.getCode());
        validationCode.setCreatedAt(entity.getCreatedAt());
        validationCode.setExpiresAt(entity.getExpiresAt());
        validationCode.setValidatedAt(entity.getValidatedAt());

        if(full){
            validationCode.setUser(UserMapper.toCore(entity.getUser(), false));
        }

        return validationCode;
    }

    public static ValidationCodeEntity toJpaEntity(ValidationCode core) {
        if(core == null) return null;

        ValidationCodeEntity entity = new ValidationCodeEntity();
        entity.setId(core.getId());
        entity.setCode(core.getCode());
        entity.setCreatedAt(core.getCreatedAt());
        entity.setValidatedAt(core.getValidatedAt());
        entity.setExpiresAt(core.getExpiresAt());
        entity.setUser(UserMapper.toJpaEntity(core.getUser()));
        return entity;
    }
}
