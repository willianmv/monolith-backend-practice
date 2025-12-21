package com.simple.blog.backend.core.usecases.reply.delete;

import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.Reply;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.IPostRepository;
import com.simple.blog.backend.core.gateway.IReplyRepository;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.ILoggerService;

public class DeleteReplyUseCase implements IDeleteReplyUseCase{

    private final IReplyRepository replyRepository;
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final ILoggerService loggerService;

    public DeleteReplyUseCase(IReplyRepository replyRepository, IPostRepository postRepository, IUserRepository userRepository, ILoggerService loggerService) {
        this.replyRepository = replyRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.loggerService = loggerService;
    }

    @Override
    public void execute(Long userId, Long replyId) {
        Reply reply = replyRepository.findById(replyId);
        Post post = postRepository.findById(reply.getPost().getId());
        User user = userRepository.findById(userId);

        if(reply.isDeleted()) throw new DomainException("Reply already deleted");

        loggerService.info("[DELETE REPLY] Checking permissions to delete reply...");

        boolean isReplyAuthor = reply.getAuthor().getId().equals(user.getId());
        boolean isPostAuthor = post.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.hasProfile("ADMIN");

        if (!isReplyAuthor && !isPostAuthor && !isAdmin) {
            throw new DomainException("User not allowed to delete this reply");
        }

        reply.markAsDeleted(userId);
        replyRepository.save(reply);

        loggerService.info("[DELETE REPLY] Reply marked as deleted");
    }
}
