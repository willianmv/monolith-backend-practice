package com.simple.blog.backend.infra.persistence.mapper;

import com.simple.blog.backend.core.domain.Profile;
import com.simple.blog.backend.core.domain.RoleType;
import com.simple.blog.backend.infra.persistence.entity.ProfileEntity;

public class ProfileMapper {

    public static Profile toCore(ProfileEntity entity){
        if(entity == null) return null;

        Profile profile = new Profile();
        profile.setId(entity.getId());
        profile.setRole(RoleType.fromString(entity.getRole().name()));
        profile.setDescription(entity.getDescription());
        return profile;
    }

    public static ProfileEntity toJpaEntity(Profile core) {
        if(core == null) return null;

        ProfileEntity entity = new ProfileEntity();
        entity.setId(core.getId());
        entity.setRole(core.getRole());
        entity.setDescription(core.getDescription());
        return entity;
    }
}
