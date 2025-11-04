package com.simple.blog.backend.core.domain;

public class Profile {

    private Long id;
    private RoleType role;
    private String description;

    public Profile() {}

    public Profile(RoleType role, String description) {
        this.role = role;
        this.description = role.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
