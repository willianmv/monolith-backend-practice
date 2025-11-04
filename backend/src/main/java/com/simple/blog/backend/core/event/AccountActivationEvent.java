package com.simple.blog.backend.core.event;

import com.simple.blog.backend.core.domain.User;

import java.time.Instant;

public record AccountActivationEvent(User user, String code, Instant expiresAt) {

}
