package com.simple.blog.backend.infra.persistence.repository;

import com.simple.blog.backend.core.domain.TagType;
import com.simple.blog.backend.infra.persistence.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TagEntityRepository extends JpaRepository <TagEntity, Long>{

    boolean existsByTag(TagType tagType);

    Optional<TagEntity> findByTag(TagType tagType);

    Set<TagEntity> findAllByTagIn(Set<TagType> tagTypes);

}
