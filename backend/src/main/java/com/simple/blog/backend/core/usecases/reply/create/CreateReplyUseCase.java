package com.simple.blog.backend.core.usecases.reply.create;

import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.Reply;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.event.ReplyToPostEvent;
import com.simple.blog.backend.core.gateway.IPostRepository;
import com.simple.blog.backend.core.gateway.IReplyRepository;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.gateway.IEventPublisher;
import com.simple.blog.backend.core.gateway.ILoggerService;

public class CreateReplyUseCase implements ICreateReplyUseCase{

    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final IReplyRepository replyRepository;
    private final IEventPublisher eventPublisher;
    private final ILoggerService loggerService;

    public CreateReplyUseCase(IUserRepository userRepository, IPostRepository postRepository, IReplyRepository replyRepository, IEventPublisher eventPublisher, ILoggerService loggerService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        this.eventPublisher = eventPublisher;
        this.loggerService = loggerService;
    }

    @Override
    public CreateReplyOutput execute(CreateReplyInput replyInput) {
        loggerService.info("[CREATE REPLY] User " + replyInput.authorId() + " replying to post " + replyInput.postId());

        User replyAuthor = userRepository.findById(replyInput.authorId());
        Post post = postRepository.findById(replyInput.postId());
        User postAuthor = post.getAuthor();

        Reply reply = Reply.create(replyInput.content(), post, replyAuthor);
        Reply savedReply = replyRepository.save(reply);

        eventPublisher.publish(new ReplyToPostEvent(postAuthor, replyAuthor, post.getTitle()));

        return new CreateReplyOutput(
                savedReply.getId(),
                savedReply.getCreatedAt(),
                replyAuthor.getUsername(),
                post.getTitle(),
                savedReply.getContent());
    }
}
