package com.simple.blog.backend.core.domain;

public class Reply extends BaseAuditable {

    private Long id;
    private String content;
    private Post post;
    private User author;

    public static Reply create(String content, Post post, User author){
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setPost(post);
        reply.setAuthor(author);
        return reply;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
