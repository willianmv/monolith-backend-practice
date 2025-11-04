package com.simple.blog.backend.infra.persistence.mapper;

import com.simple.blog.backend.core.domain.Reply;
import com.simple.blog.backend.infra.persistence.entity.PostEntity;
import com.simple.blog.backend.infra.persistence.entity.ReplyEntity;
import com.simple.blog.backend.infra.persistence.entity.UserEntity;

public class ReplyMapper {

    public static Reply toCore(ReplyEntity entity, boolean full){
        if(entity == null) return null;

        Reply reply = new Reply();
        reply.setId(entity.getId());
        reply.setContent(entity.getContent());
        reply.setCreatedAt(entity.getCreatedAt());
        reply.setUpdatedAt(entity.getUpdatedAt());
        reply.setUpdatedBy(entity.getUpdatedBy());
        reply.setDeleted(entity.getDeletedAt());

        if(full){
            reply.setAuthor(UserMapper.toCore(entity.getAuthor(), false));
            reply.setPost(PostMapper.toCore(entity.getPost(), false));
        }

        return reply;
    }

    public static ReplyEntity toJpaEntity(Reply core) {
        if(core == null) return null;

        ReplyEntity entity = new ReplyEntity();
        entity.setId(core.getId());
        entity.setContent(core.getContent());
        entity.setCreatedAt(core.getCreatedAt());
        entity.setUpdatedAt(core.getUpdatedAt());
        entity.setUpdatedBy(core.getUpdatedBy());
        entity.setDeletedAt(core.getDeletedAt());

        PostEntity postEntity = new PostEntity();
        postEntity.setId(core.getPost().getId());
        entity.setPost(postEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(core.getAuthor().getId());
        entity.setAuthor(userEntity);

        return entity;
    }
}
