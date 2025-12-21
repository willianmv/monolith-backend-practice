package com.simple.blog.backend.infra.config.beans;

import com.simple.blog.backend.core.gateway.*;
import com.simple.blog.backend.core.usecases.auth.activation.ActivateAccountUseCase;
import com.simple.blog.backend.core.usecases.auth.activation.IActivateAccountUseCase;
import com.simple.blog.backend.core.usecases.auth.login.ILoginUseCase;
import com.simple.blog.backend.core.usecases.auth.login.LoginUseCase;
import com.simple.blog.backend.core.usecases.auth.logout.ILogoutUseCase;
import com.simple.blog.backend.core.usecases.auth.logout.LogoutUseCase;
import com.simple.blog.backend.core.usecases.auth.refresh.IRefreshTokenUseCase;
import com.simple.blog.backend.core.usecases.auth.refresh.RefreshTokenUseCase;
import com.simple.blog.backend.core.usecases.auth.register.IRegisterUseCase;
import com.simple.blog.backend.core.usecases.auth.register.RegisterUseCase;
import com.simple.blog.backend.core.usecases.post.create.CreatePostUseCase;
import com.simple.blog.backend.core.usecases.post.create.ICreatePostUseCase;
import com.simple.blog.backend.core.usecases.post.delete.DeletePostUseCase;
import com.simple.blog.backend.core.usecases.post.delete.IDeletePostUseCase;
import com.simple.blog.backend.core.usecases.post.detailed.GetPostUseCase;
import com.simple.blog.backend.core.usecases.post.detailed.IGetPostUseCase;
import com.simple.blog.backend.core.usecases.post.summary.GetPostsUseCase;
import com.simple.blog.backend.core.usecases.post.summary.IGetPostsUseCase;
import com.simple.blog.backend.core.usecases.reply.create.CreateReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.create.ICreateReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.delete.DeleteReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.delete.IDeleteReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.get.GetRepliesUseCase;
import com.simple.blog.backend.core.usecases.reply.get.IGetRepliesUseCase;
import com.simple.blog.backend.core.validator.CreatePostValidator;
import com.simple.blog.backend.infra.gateway.LoggerService;
import com.simple.blog.backend.infra.gateway.SpringEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationUseCaseBeans {

    @Bean
    public IEventPublisher eventPublisherGateway(ApplicationEventPublisher springPublisher){
        return new SpringEventPublisher(springPublisher);
    }

    @Bean
    public IRegisterUseCase registerUseCase(
            IUserRepository userRepository, IProfileRepository profileRepository,
            IValidationCodeRepository validationCodeRepository, ICodeService codeService,
            IPasswordService passwordService, IEventPublisher eventPublisher) {

        ILoggerService loggerService = new LoggerService(RegisterUseCase.class);
        return new RegisterUseCase(
                userRepository, profileRepository,
                validationCodeRepository, codeService,
                passwordService, eventPublisher, loggerService);
    }

    @Bean
    public IActivateAccountUseCase activateAccountUseCase(
            IValidationCodeRepository validationCodeRepository,
            IUserRepository userRepository,
            IEventPublisher eventPublisher){

        ILoggerService loggerService = new LoggerService(ActivateAccountUseCase.class);
        return new ActivateAccountUseCase(
                validationCodeRepository,
                userRepository,
                eventPublisher, loggerService);
    }

    @Bean
    public ILoginUseCase loginUseCase(
            IJwtService jwtService,
            IUserRepository userRepository,
            IPasswordService passwordService,
            IRefreshTokenService refreshTokenService){

        ILoggerService loggerService = new LoggerService(LoginUseCase.class);
        return new LoginUseCase(
                jwtService,
                userRepository,
                passwordService,
                refreshTokenService, loggerService);
    }

    @Bean
    public IRefreshTokenUseCase refreshTokenUseCase(
            IJwtService jwtService,
            IUserRepository userRepository,
            IRefreshTokenRepository refreshTokenService
    ){
        ILoggerService loggerService = new LoggerService(RefreshTokenUseCase.class);
        return new RefreshTokenUseCase(
                jwtService,
                userRepository,
                refreshTokenService, loggerService);
    }

    @Bean
    public ILogoutUseCase logoutUseCase(
            IRefreshTokenService refreshTokenService,
            IUserRepository userRepository){

        ILoggerService loggerService = new LoggerService(LogoutUseCase.class);
        return new LogoutUseCase(refreshTokenService, userRepository, loggerService);
    }


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
