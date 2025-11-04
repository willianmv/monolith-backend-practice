package com.simple.blog.backend.infra.persistence.mapper;

import com.simple.blog.backend.core.domain.Tag;
import com.simple.blog.backend.core.domain.TagType;
import com.simple.blog.backend.infra.persistence.entity.TagEntity;

public class TagMapper {

    public static Tag toCore(TagEntity entity){
        if(entity == null) return null;

        Tag tag = new Tag();
        tag.setId(entity.getId());
        tag.setTag(TagType.fromString(entity.getTag().name()));
        return tag;
    }

    public static TagEntity toJpaEntity(Tag core){
        if(core == null) return null;

        TagEntity tagEntity = new TagEntity();
        tagEntity.setId(core.getId());
        tagEntity.setTag(core.getTag());
        return tagEntity;
    }

}
