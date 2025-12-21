package com.simple.blog.backend.infra.config.security;

import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.gateway.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserRepository userRepositoryGateway;

    public UserDetailsServiceImpl(IUserRepository userRepositoryGateway) {
        this.userRepositoryGateway = userRepositoryGateway;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepositoryGateway.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new SimpleBlogUserDetails(user);
    }
}
