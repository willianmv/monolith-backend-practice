package com.simple.blog.backend.infra.config.usecase;

import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.gateway.repository.ITagRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.ILoggerService;
import com.simple.blog.backend.core.usecases.post.create.ICreatePostUseCase;
import com.simple.blog.backend.core.usecases.post.create.CreatePostUseCase;
import com.simple.blog.backend.core.usecases.post.delete.DeletePostUseCase;
import com.simple.blog.backend.core.usecases.post.delete.IDeletePostUseCase;
import com.simple.blog.backend.core.usecases.post.detailed.GetPostUseCase;
import com.simple.blog.backend.core.usecases.post.detailed.IGetPostUseCase;
import com.simple.blog.backend.core.usecases.post.summary.GetPostsUseCase;
import com.simple.blog.backend.core.usecases.post.summary.IGetPostsUseCase;
import com.simple.blog.backend.core.validator.CreatePostValidator;
import com.simple.blog.backend.infra.gateway.service.LoggerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostUseCases {

    @Bean
    public ICreatePostUseCase createPostUseCase(
            IPostRepository postRepository, CreatePostValidator createPostValidator,
            IUserRepository userRepository, ITagRepository tagRepository
    ){
        ILoggerService loggerService = new LoggerService(CreatePostUseCase.class);
        return new CreatePostUseCase(
                postRepository, userRepository,
                tagRepository, loggerService,
                createPostValidator);
    }

    @Bean
    public IGetPostsUseCase getPostsUseCase(IPostRepository postRepository){
        ILoggerService loggerService = new LoggerService(GetPostsUseCase.class);
        return new GetPostsUseCase(postRepository, loggerService);
    }

    @Bean
    public IGetPostUseCase getPostUseCase(IPostRepository postRepository, IUserRepository userRepository){
        ILoggerService loggerService = new LoggerService(GetPostUseCase.class);
        return new GetPostUseCase(postRepository,userRepository, loggerService);
    }

    @Bean
    public IDeletePostUseCase deletePostUseCase(IPostRepository postRepository, IUserRepository userRepository){
        ILoggerService loggerService = new LoggerService(DeletePostUseCase.class);
        return new DeletePostUseCase(postRepository, userRepository, loggerService);
    }

}
