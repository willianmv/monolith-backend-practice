package com.simple.blog.backend.core.usecases.reply.get;

import com.simple.blog.backend.core.domain.DomainPage;

public interface IGetRepliesUseCase {

    public DomainPage<ReplySummaryOutput> execute(Long postId, int page, int size);

}
