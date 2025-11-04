package com.simple.blog.backend.infra.persistence.repository;

import com.simple.blog.backend.infra.persistence.entity.ValidationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface ValidationCodeEntityRepository extends JpaRepository <ValidationCodeEntity, Long>{

    @Query("""
            SELECT COUNT(vc) > 0
            FROM ValidationCodeEntity vc
            WHERE vc.user.id = :userId AND vc.expiresAt > :now AND vc.validatedAt IS NULL
            """)
    boolean existsValidByUserId(long userId, Instant now);

    Optional<ValidationCodeEntity> findByCode(String code);

    Optional<ValidationCodeEntity> findTopByUserIdOrderByCreatedAtDesc(long userId);
}
