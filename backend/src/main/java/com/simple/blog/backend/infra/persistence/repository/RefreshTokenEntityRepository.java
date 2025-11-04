package com.simple.blog.backend.infra.persistence.repository;

import com.simple.blog.backend.infra.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteByUserId(long userId);

    Optional<RefreshTokenEntity> findByUserId(long userId);

}
