package com.simple.blog.backend.infra.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_replies")
public class ReplyEntity extends BaseAuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne()
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne()
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

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

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
