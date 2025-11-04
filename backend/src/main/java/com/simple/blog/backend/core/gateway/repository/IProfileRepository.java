package com.simple.blog.backend.core.gateway.repository;

import com.simple.blog.backend.core.domain.Profile;
import com.simple.blog.backend.core.domain.RoleType;

public interface IProfileRepository {

    Profile findByRole(RoleType roleType);

    boolean existsByRole(RoleType roleType);

    void save(Profile profile);

}
