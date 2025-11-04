package com.simple.blog.backend.core.domain;

public class Tag {

    private Long id;
    private TagType tag;

    public Long getId() {
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
