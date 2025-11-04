package com.simple.blog.backend.infra.persistence.entity;

import com.simple.blog.backend.core.domain.TagType;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_tags")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private TagType tag;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TagType getTag() {
        return tag;
    }

    public void setTag(TagType tag) {
        this.tag = tag;
    }
}
