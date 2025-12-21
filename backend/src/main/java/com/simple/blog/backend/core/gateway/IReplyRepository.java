package com.simple.blog.backend.core.gateway;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.domain.Reply;

public interface IReplyRepository {

    Reply save(Reply reply);

    Reply findById(Long replyId);

    DomainPage<Reply> findByPostId(Long postId, int page, int size);

}
