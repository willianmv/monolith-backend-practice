package com.simple.blog.backend.core.domain;

import java.time.Instant;

public class ValidationCode {

    private Long id;
    private String code;
    private Instant createdAt;
    private Instant validatedAt;
    private Instant expiresAt;
    private User user;

    public static ValidationCode createForUser(String code, User user){
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(300);

        ValidationCode validationCode = new ValidationCode();
        validationCode.setCode(code);
        validationCode.setUser(user);
        validationCode.setCreatedAt(now);
        validationCode.setExpiresAt(expiresAt);
        return validationCode;
    }

    public boolean isExpired(){
        return this.expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public boolean isValidated(){
        return this.validatedAt != null;
    }

    public void markAsValidated(){
        this.validatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getValidatedAt() {
        return validatedAt;
    }

    public void setValidatedAt(Instant validatedAt) {
        this.validatedAt = validatedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
