package com.simple.blog.backend.core.gateway.repository;

import com.simple.blog.backend.core.domain.User;

import java.util.Optional;

public interface IUserRepository {

    void save(User user);

    Optional<User> findByEmail(String email);

    User findById(long id);

    boolean existsByUsername(String username);

    boolean existsById(long id);

}
