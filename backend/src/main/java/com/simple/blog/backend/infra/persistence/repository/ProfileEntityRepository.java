package com.simple.blog.backend.infra.persistence.repository;

import com.simple.blog.backend.core.domain.RoleType;
import com.simple.blog.backend.infra.persistence.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileEntityRepository extends JpaRepository <ProfileEntity, Long>{

    Optional<ProfileEntity> findByRole(RoleType role);

    boolean existsByRole(RoleType roleType);

}
