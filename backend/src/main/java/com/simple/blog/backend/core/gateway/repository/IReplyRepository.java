package com.simple.blog.backend.core.gateway.repository;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.domain.Reply;
import com.simple.blog.backend.core.usecases.reply.get.ReplySummaryOutput;

public interface IReplyRepository {

    Reply save(Reply reply);

    Reply findById(Long replyId);

    DomainPage<Reply> findByPostId(Long postId, int page, int size);

}
