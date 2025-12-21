package com.simple.blog.backend.core.usecases.post.detailed;

import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.gateway.IPostRepository;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.ILoggerService;

import java.util.Set;
import java.util.stream.Collectors;

public class GetPostUseCase implements IGetPostUseCase{

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final ILoggerService loggerService;

    public GetPostUseCase(IPostRepository postRepository, IUserRepository userRepository, ILoggerService loggerService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.loggerService = loggerService;
    }

    @Override
    public DetailedPostOutput execute(Long postId) {
        loggerService.info("[GET POST BY ID] Fetching post: " + postId);

        Post post = postRepository.findById(postId);
        User author = userRepository.findById(post.getAuthor().getId());
        Set<String> tags = post.getTags().stream().map(t -> t.getTag().name()).collect(Collectors.toSet());
        int replyCount = post.getReplies().size();

        return new DetailedPostOutput(
                post.getId(),
                author.getUsername(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                tags,
                replyCount,
                post.getCreatedAt()
        );
    }
}
