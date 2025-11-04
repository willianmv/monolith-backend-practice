package com.simple.blog.backend.infra.gateway.repository;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.usecases.post.summary.PostQueryFilter;
import com.simple.blog.backend.core.usecases.post.summary.SummaryPostOutput;
import com.simple.blog.backend.infra.persistence.entity.TagEntity;
import com.simple.blog.backend.infra.persistence.mapper.PostMapper;
import com.simple.blog.backend.infra.persistence.entity.PostEntity;
import com.simple.blog.backend.infra.persistence.repository.PostEntityRepository;
import com.simple.blog.backend.infra.persistence.specs.PostSpecifications;
import com.simple.blog.backend.infra.persistence.specs.SpecHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostRepository implements IPostRepository {

    private final PostEntityRepository postEntityRepository;

    public PostRepository(PostEntityRepository postEntityRepository) {
        this.postEntityRepository = postEntityRepository;
    }

    @Override
    public Post save(Post post) {
        PostEntity postEntity = postEntityRepository.save(PostMapper.toJpaEntity(post));
        return PostMapper.toCore(postEntity, true);
    }

    @Override
    public Post findById(long id) {
        return this.postEntityRepository.findById(id)
                .filter(p -> p.getDeletedAt() == null)
                .map(p -> PostMapper.toCore(p, true))
                .orElseThrow(() -> new EntityNotFoundException("Post not found by id: "+ id));
    }

    @Override
    public DomainPage<SummaryPostOutput> findSummaryPosts(PostQueryFilter filter, int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                filter.sortOrder() == PostQueryFilter.SortOrder.OLDEST
                        ? Sort.by("createdAt").ascending()
                        : Sort.by("createdAt").descending()
        );

        Specification<PostEntity> spec = PostSpecifications.notDeleted();

        if(filter.tags() != null && !filter.tags().isEmpty()){
            spec = SpecHelper.andSpec(spec, PostSpecifications.hasTags(filter.tags().stream()
                    .map(String::toUpperCase).collect(Collectors.toSet())));
        }

        if(filter.titleContains() != null && !filter.titleContains().isBlank()){
            spec = SpecHelper.andSpec(spec, PostSpecifications.titleContains(filter.titleContains()));
        }

        if(filter.authorId() != null){
            spec = SpecHelper.andSpec(spec, PostSpecifications.hasAuthorId(filter.authorId()));
        }

        Page<PostEntity> postPage = postEntityRepository.findAll(spec, pageable);

        List<SummaryPostOutput> summaryPostList = postPage.getContent().stream()
                .map(postEntity -> new SummaryPostOutput(
                        postEntity.getId(),
                        postEntity.getTitle(),
                        postEntity.getAuthor().getUsername(),
                        postEntity.getImageUrl(),
                        postEntity.getReplies().size(),
                        postEntity.getTags().stream().map(TagEntity::getTag).map(Enum::name).collect(Collectors.toSet()),
                        postEntity.getCreatedAt()
                ))
                .toList();

        return new DomainPage<>(postPage.getNumber(), postPage.getSize(), postPage.getTotalElements(), summaryPostList);
    }
}
