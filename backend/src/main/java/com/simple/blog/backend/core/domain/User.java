package com.simple.blog.backend.core.domain;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class User extends BaseAuditable {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private boolean isActive = false;

    private Set<Profile> profiles = new HashSet<>();
    private Set<Post> posts = new HashSet<>();
    private Set<Reply> replies = new HashSet<>();

    public User() {}

    public static User createBasic(String name, String username, String email, String encodedPassword){
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setActive(false);
        user.setDeleted(null);
        user.setCreatedAt(Instant.now());
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public Set<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
    }

    public boolean hasProfile(String profileName){
        return profiles.stream().anyMatch(profile ->
                profile.getRole().name().equalsIgnoreCase(profileName));
    }
}
