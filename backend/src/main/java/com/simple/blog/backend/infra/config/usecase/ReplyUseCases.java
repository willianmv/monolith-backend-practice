package com.simple.blog.backend.infra.config.usecase;

import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.gateway.repository.IReplyRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.IEventPublisher;
import com.simple.blog.backend.core.gateway.service.ILoggerService;
import com.simple.blog.backend.core.usecases.reply.create.CreateReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.create.ICreateReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.delete.DeleteReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.delete.IDeleteReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.get.GetRepliesUseCase;
import com.simple.blog.backend.core.usecases.reply.get.IGetRepliesUseCase;
import com.simple.blog.backend.infra.gateway.service.LoggerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReplyUseCases {

    @Bean
    public ICreateReplyUseCase createReplyUseCase(
            IUserRepository userRepository, IPostRepository postRepository,
            IReplyRepository replyRepository, IEventPublisher eventPublisher){
        ILoggerService loggerService = new LoggerService(CreateReplyUseCase.class);
        return new CreateReplyUseCase(userRepository, postRepository, replyRepository, eventPublisher,loggerService);
    }

    @Bean
    public IGetRepliesUseCase getRepliesUseCase(IReplyRepository replyRepository, IPostRepository postRepository){
        ILoggerService loggerService = new LoggerService(GetRepliesUseCase.class);
        return new GetRepliesUseCase(replyRepository, postRepository, loggerService);
    }

    @Bean
    public IDeleteReplyUseCase deleteReplyUseCase(IReplyRepository replyRepository, IPostRepository postRepository, IUserRepository userRepository){
        ILoggerService loggerService = new LoggerService(DeleteReplyUseCase.class);
        return new DeleteReplyUseCase(replyRepository, postRepository, userRepository, loggerService);
    }

}
