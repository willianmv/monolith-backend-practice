package com.simple.blog.backend.core.usecases.post.summary;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.gateway.service.ILoggerService;

public class GetPostsUseCase implements IGetPostsUseCase{

    private final IPostRepository postRepository;
    private final ILoggerService loggerService;

    public GetPostsUseCase(IPostRepository postRepository, ILoggerService loggerService) {
        this.postRepository = postRepository;
        this.loggerService = loggerService;
    }

    @Override
    public DomainPage<SummaryPostOutput> execute(PostQueryFilter filter, int page, int size) {
        loggerService.info("[SUMMARY POSTS] ✔ looking for page: "+ page + ", size: " + size);
        return postRepository.findSummaryPosts(filter, page, size);
    }
}
