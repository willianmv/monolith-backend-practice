package com.simple.blog.backend.core.usecases.reply.get;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.Reply;
import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.gateway.repository.IReplyRepository;
import com.simple.blog.backend.core.gateway.service.ILoggerService;

import java.util.List;

public class GetRepliesUseCase implements IGetRepliesUseCase{

    private final IReplyRepository replyRepository;
    private final IPostRepository postRepository;
    private final ILoggerService loggerService;

    public GetRepliesUseCase(IReplyRepository replyRepository, IPostRepository postRepository, ILoggerService loggerService) {
        this.replyRepository = replyRepository;
        this.postRepository = postRepository;
        this.loggerService = loggerService;
    }

    @Override
    public DomainPage<ReplySummaryOutput> execute(Long postId, int page, int size) {
        loggerService.info("[GET REPLIES BY POST] postId:" + postId + " - page:" + page);

        Post post = postRepository.findById(postId);

        DomainPage<Reply> replies = replyRepository.findByPostId(postId, page, size);

        List<ReplySummaryOutput> repliesList = replies.items().stream()
                .map(r -> new ReplySummaryOutput(
                        r.getId(), r.getAuthor().getUsername(), r.getContent(), r.getCreatedAt())).toList();

        return new DomainPage<>(
                replies.pageNumber(),
                replies.pageSize(),
                replies.totalItems(),
                repliesList
        );
    }
}
