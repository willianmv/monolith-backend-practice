package com.simple.blog.backend.core.gateway.repository;

import com.simple.blog.backend.core.domain.Tag;
import com.simple.blog.backend.core.domain.TagType;

import java.util.Set;

public interface ITagRepository {

    void save(Tag tag);

    boolean existsByTag(TagType tagType);

    Set<String> findAllTagNames();

    Set<Tag> findAllByTags(Set<TagType> tags);

}
