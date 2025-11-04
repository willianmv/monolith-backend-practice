package com.simple.blog.backend.infra.gateway.repository;

import com.simple.blog.backend.core.domain.Tag;
import com.simple.blog.backend.core.domain.TagType;
import com.simple.blog.backend.core.gateway.repository.ITagRepository;
import com.simple.blog.backend.infra.persistence.mapper.TagMapper;
import com.simple.blog.backend.infra.persistence.entity.TagEntity;
import com.simple.blog.backend.infra.persistence.repository.TagEntityRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TagRepository implements ITagRepository {

    private final TagEntityRepository tagEntityRepository;

    public TagRepository(TagEntityRepository tagEntityRepository) {
        this.tagEntityRepository = tagEntityRepository;
    }

    @Override
    public void save(Tag tag) {
        TagEntity tagEntity = TagMapper.toJpaEntity(tag);
        this.tagEntityRepository.save(tagEntity);
    }

    @Override
    public boolean existsByTag(TagType tagType) {
        return this.tagEntityRepository.existsByTag(tagType);
    }


    @Override
    public Set<String> findAllTagNames() {
        return this.tagEntityRepository.findAll().stream()
                .map(tag -> tag.getTag().name())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Tag> findAllByTags(Set<TagType> tags) {
        Set<TagEntity> tagEntities = this.tagEntityRepository.findAllByTagIn(tags);
        return tagEntities.stream()
                .map(TagMapper::toCore)
                .collect(Collectors.toSet());
    }


}
