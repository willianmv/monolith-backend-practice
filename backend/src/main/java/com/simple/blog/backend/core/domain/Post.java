package com.simple.blog.backend.core.domain;

import java.util.HashSet;
import java.util.Set;

public class Post extends BaseAuditable {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private User author;

    private Set<Tag> tags = new HashSet<>();
    private Set<Reply> replies = new HashSet<>();

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image_url) {
        this.imageUrl = image_url;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
    }

    public static Post create(String title, String content, String imageUrl, User author, Set<Tag> tags){
        Post post  = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setImageUrl(imageUrl != null ? imageUrl : "");
        post.setAuthor(author);
        post.setTags(tags != null ? tags : new HashSet<>());
        post.setReplies(new HashSet<>());
        return post;
    }
}
