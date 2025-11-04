package com.simple.blog.backend.core.usecases.post.delete;

import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.ILoggerService;

public class DeletePostUseCase implements IDeletePostUseCase{

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final ILoggerService loggerService;

    public DeletePostUseCase(IPostRepository postRepository, IUserRepository userRepository, ILoggerService loggerService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.loggerService = loggerService;
    }

    @Override
    public void execute(Long postId, Long userId) {
        Post post = postRepository.findById(postId);
        User user = userRepository.findById(userId);

        if(post.isDeleted()) throw new DomainException("Post already deleted");

        loggerService.info("[DELETE POST] Checking permissions to delete post...");
        boolean isOwner = post.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.hasProfile("ADMIN");

        if(!isOwner && !isAdmin) throw new DomainException("User not allowed to delete this post");

        post.markAsDeleted(userId);
        loggerService.info("[DELETE POST] Post marked as deleted");
        postRepository.save(post);
    }
}
