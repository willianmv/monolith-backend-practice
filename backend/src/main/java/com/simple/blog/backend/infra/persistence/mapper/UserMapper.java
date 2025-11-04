package com.simple.blog.backend.infra.persistence.mapper;

import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.infra.persistence.entity.UserEntity;

import java.util.stream.Collectors;

public class UserMapper {

    public static User toCore(UserEntity entity, boolean full){
        if(entity == null) return null;

        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setActive(entity.getActive());
        user.setDeleted(entity.getDeletedAt());
        user.setCreatedAt(entity.getCreatedAt());

        if(full){
            user.setProfiles(
                    entity.getProfiles().stream()
                    .map(ProfileMapper::toCore)
                            .collect(Collectors.toSet()));
        }

        return user;
    }

    public static UserEntity toJpaEntity(User core){
        if(core == null) return null;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(core.getId());
        userEntity.setName(core.getName());
        userEntity.setUsername(core.getUsername());
        userEntity.setEmail(core.getEmail());
        userEntity.setPassword(core.getPassword());
        userEntity.setCreatedAt(core.getCreatedAt());
        userEntity.setActive(core.isActive());

        userEntity.setProfiles(
                core.getProfiles().stream()
                        .map(ProfileMapper::toJpaEntity)
                        .collect(Collectors.toSet())
        );

        return userEntity;
    }
}
