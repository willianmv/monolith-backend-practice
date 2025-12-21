package com.simple.blog.backend.core.gateway;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.usecases.post.summary.PostQueryFilter;
import com.simple.blog.backend.core.usecases.post.summary.SummaryPostOutput;

public interface IPostRepository {

    Post save(Post post);

    Post findById(long id);

    DomainPage<SummaryPostOutput> findSummaryPosts(PostQueryFilter filter, int page, int size);
}
