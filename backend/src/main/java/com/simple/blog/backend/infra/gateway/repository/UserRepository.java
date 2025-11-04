package com.simple.blog.backend.infra.gateway.repository;

import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.infra.persistence.mapper.UserMapper;
import com.simple.blog.backend.infra.persistence.entity.ProfileEntity;
import com.simple.blog.backend.infra.persistence.entity.UserEntity;
import com.simple.blog.backend.infra.persistence.repository.ProfileEntityRepository;
import com.simple.blog.backend.infra.persistence.repository.UserEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserRepository implements IUserRepository {

    private final UserEntityRepository userEntityRepository;
    private final ProfileEntityRepository profileEntityRepository;

    public UserRepository(UserEntityRepository userEntityRepository, ProfileEntityRepository profileEntityRepository) {
        this.userEntityRepository = userEntityRepository;
        this.profileEntityRepository = profileEntityRepository;
    }

    @Override
    public void save(User user) {
        UserEntity userEntity;

        if(user.getId() == null){
            userEntity = UserMapper.toJpaEntity(user);

            Set<ProfileEntity> profiles = user.getProfiles().stream()
                    .map(profile -> profileEntityRepository.findByRole(profile.getRole())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Profile not found by role: "+ profile.getRole().name())))
                    .collect(Collectors.toSet());

            userEntity.setProfiles(profiles);

        } else{

            userEntity = userEntityRepository.findById(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            userEntity.setActive(user.isActive());
            userEntity.setName(user.getName() != null ? user.getName() : userEntity.getName());
            userEntity.setUsername(user.getUsername() != null ? user.getUsername() : userEntity.getUsername());
            userEntity.setEmail(user.getEmail() != null ? user.getEmail() : userEntity.getEmail());
        }

        userEntityRepository.save(userEntity);
        user.setId(userEntity.getId());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userEntityRepository.findByEmail(email)
                .map(u -> UserMapper.toCore(u, true));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userEntityRepository.existsByUsername(username);
    }

    @Override
    public User findById(long id) {
        return userEntityRepository.findById(id)
                .map( u -> UserMapper.toCore(u, true))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public boolean existsById(long id) {
        return userEntityRepository.existsById(id);
    }
}
