package com.simple.blog.backend.core.usecases.post.summary;

import com.simple.blog.backend.core.domain.DomainPage;

public interface IGetPostsUseCase {

    DomainPage<SummaryPostOutput> execute(PostQueryFilter filter, int page, int size);

}
