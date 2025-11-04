package com.simple.blog.backend.infra.persistence.specs;

import com.simple.blog.backend.infra.persistence.entity.PostEntity;
import com.simple.blog.backend.infra.persistence.entity.TagEntity;
import com.simple.blog.backend.infra.persistence.entity.UserEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class PostSpecifications {

    public static Specification<PostEntity> notDeleted(){
        return (root, query, cb)
                -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<PostEntity> hasTags(Set<String> tags){
        return (root, query, cb) -> {
          if(tags == null || tags.isEmpty()) return null;
          Join<PostEntity, TagEntity> tagJoin = root.join("tags");
          return tagJoin.get("tag").in(tags);
        };
    }

    public static Specification<PostEntity> titleContains(String title){
        return (root, query, cb) -> {
          if(title == null || title.isBlank()) return null;
          return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<PostEntity> hasAuthorId(Long authorId) {
        return (root, query, cb) -> {
            if (authorId == null) return null;
            Join<PostEntity, UserEntity> authorJoin = root.join("author");
            return cb.equal(authorJoin.get("id"), authorId);
        };
    }

}
