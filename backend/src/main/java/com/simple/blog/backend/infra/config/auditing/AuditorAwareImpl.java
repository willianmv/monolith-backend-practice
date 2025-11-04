package com.simple.blog.backend.infra.config.auditing;

import com.simple.blog.backend.infra.config.security.SimpleBlogUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if(principal instanceof SimpleBlogUserDetails simpleBlogUserDetails){
            return Optional.of(simpleBlogUserDetails.getId());
        }

        return Optional.empty();
    }
}
