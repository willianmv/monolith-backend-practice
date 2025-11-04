package com.simple.blog.backend.infra.gateway.repository;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.domain.Reply;
import com.simple.blog.backend.core.gateway.repository.IReplyRepository;
import com.simple.blog.backend.core.usecases.reply.get.ReplySummaryOutput;
import com.simple.blog.backend.infra.persistence.entity.ReplyEntity;
import com.simple.blog.backend.infra.persistence.mapper.ReplyMapper;
import com.simple.blog.backend.infra.persistence.repository.ReplyEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ReplyRepository implements IReplyRepository {

    private final ReplyEntityRepository replyEntityRepository;

    public ReplyRepository(ReplyEntityRepository replyEntityRepository) {
        this.replyEntityRepository = replyEntityRepository;
    }

    @Override
    public Reply save(Reply reply) {
        ReplyEntity replyEntity = replyEntityRepository.save(ReplyMapper.toJpaEntity(reply));
        return ReplyMapper.toCore(replyEntity, true);
    }

    @Override
    public Reply findById(Long replyId) {
        return replyEntityRepository.findById(replyId)
                .filter(r -> r.getDeletedAt() == null)
                .map(r -> ReplyMapper.toCore(r, true))
                .orElseThrow(() -> new EntityNotFoundException("Reply not found with id: "+ replyId));
    }

    @Override
    public DomainPage<Reply> findByPostId(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReplyEntity> replies = replyEntityRepository.findByPostId(postId, pageable);

        return new DomainPage<>(
                replies.getNumber(),
                replies.getSize(),
                replies.getTotalElements(),
                replies.getContent().stream().map(r -> ReplyMapper.toCore(r, true)).toList());
    }
}
