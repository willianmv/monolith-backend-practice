package com.simple.blog.backend.core.domain;

public enum RoleType {

    REGULAR("Can operate over it's own resources"),
    ADMIN("Can operate over all posts");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public static RoleType fromString(String name){
        for(RoleType role : RoleType.values()){
            if(name.equalsIgnoreCase(role.name())) return role;
        }
        throw  new IllegalArgumentException("invalid role: "+ name);
    }
}
