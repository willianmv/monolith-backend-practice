package com.simple.blog.backend.infra.persistence.mapper;

import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.infra.persistence.entity.PostEntity;
import com.simple.blog.backend.infra.persistence.entity.TagEntity;
import com.simple.blog.backend.infra.persistence.entity.UserEntity;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class PostMapper {

    public static Post toCore(PostEntity entity, boolean full){
        if(entity == null) return null;

        Post post  = new Post();
        post.setId(entity.getId());
        post.setTitle(entity.getTitle());
        post.setContent(entity.getContent());
        post.setImageUrl(entity.getImageUrl());
        post.setCreatedAt(entity.getCreatedAt());
        post.setUpdatedAt(entity.getUpdatedAt());
        post.setUpdatedBy(entity.getUpdatedBy());
        post.setDeleted(entity.getDeletedAt());

        if(full){
            post.setAuthor(UserMapper.toCore(entity.getAuthor(), false));

            post.setTags(
                    entity.getTags().stream()
                    .map(TagMapper::toCore)
                            .collect(Collectors.toSet())
            );

            post.setReplies(
                    entity.getReplies() != null
                            ? entity.getReplies().stream()
                            .map(reply -> ReplyMapper.toCore(reply, false))
                            .collect(Collectors.toSet())
                            : Collections.emptySet()
            );
        }

        return post;
    }

    public static PostEntity toJpaEntity(Post core){
        if(core == null) return null;

        PostEntity postEntity = new PostEntity();
        postEntity.setId(core.getId());
        postEntity.setTitle(core.getTitle());
        postEntity.setContent(core.getContent());
        postEntity.setImageUrl(core.getImageUrl());
        postEntity.setCreatedAt(core.getCreatedAt());
        postEntity.setUpdatedAt(core.getUpdatedAt());
        postEntity.setUpdatedBy(core.getUpdatedBy());
        postEntity.setDeletedAt(core.getDeletedAt());

        UserEntity userEntity = new UserEntity();
        userEntity.setId(core.getAuthor().getId());
        postEntity.setAuthor(userEntity);

        Set<TagEntity> tagEntities = core.getTags().stream()
                .map(TagMapper::toJpaEntity)
                .collect(Collectors.toSet());
        postEntity.setTags(tagEntities);

        return postEntity;
    }
}
