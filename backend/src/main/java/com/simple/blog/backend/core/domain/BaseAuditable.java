package com.simple.blog.backend.core.domain;

import java.time.Instant;

public abstract class BaseAuditable {

    protected Instant createdAt;
    protected Instant updatedAt;
    protected Instant deletedAt;;
    protected Long updatedBy;

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setDeleted(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public void markAsDeleted(Long userId){
        if(this.deletedAt == null){
            this.deletedAt = Instant.now();
            this.updatedBy = userId;
        }
    }

    public boolean isDeleted(){
        return this.deletedAt != null;
    }
}
