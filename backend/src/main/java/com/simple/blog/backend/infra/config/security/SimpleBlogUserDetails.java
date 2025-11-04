package com.simple.blog.backend.infra.config.security;

import com.simple.blog.backend.core.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SimpleBlogUserDetails implements UserDetails {

    private final User user;

    public SimpleBlogUserDetails(User user) {
        this.user = user;
    }

    public User getCoreUser(){
        return this.user;
    }

    public long getId(){
        return this.user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getProfiles().stream()
                .map(profile -> new SimpleGrantedAuthority("ROLE_" + profile.getRole().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
