package com.simple.blog.backend.infra.persistence.repository;

import com.simple.blog.backend.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository <UserEntity, Long>{

    @EntityGraph(attributePaths = "profiles")
    Optional<UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);
}
