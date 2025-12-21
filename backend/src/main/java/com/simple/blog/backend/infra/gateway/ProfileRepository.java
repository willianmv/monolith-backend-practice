package com.simple.blog.backend.infra.gateway;

import com.simple.blog.backend.core.domain.Profile;
import com.simple.blog.backend.core.domain.RoleType;
import com.simple.blog.backend.core.gateway.IProfileRepository;
import com.simple.blog.backend.infra.persistence.mapper.ProfileMapper;
import com.simple.blog.backend.infra.persistence.entity.ProfileEntity;
import com.simple.blog.backend.infra.persistence.repository.ProfileEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class ProfileRepository implements IProfileRepository {

    private final ProfileEntityRepository profileEntityRepository;

    public ProfileRepository(ProfileEntityRepository profileEntityRepository) {
        this.profileEntityRepository = profileEntityRepository;
    }

    @Override
    public void save(Profile profile) {
        ProfileEntity profileEntity = ProfileMapper.toJpaEntity(profile);
        profileEntityRepository.save(profileEntity);
    }

    @Override
    public Profile findByRole(RoleType roleType) {
        return profileEntityRepository.findByRole(roleType)
                .map(ProfileMapper::toCore)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: "+roleType.name()));
    }

    @Override
    public boolean existsByRole(RoleType roleType) {
        return profileEntityRepository.existsByRole(roleType);
    }
}
