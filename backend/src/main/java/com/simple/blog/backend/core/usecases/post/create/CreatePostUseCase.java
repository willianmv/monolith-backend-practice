package com.simple.blog.backend.core.usecases.post.create;

import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.Tag;
import com.simple.blog.backend.core.domain.TagType;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.gateway.repository.ITagRepository;
import com.simple.blog.backend.core.gateway.repository.IUserRepository;
import com.simple.blog.backend.core.gateway.service.ILoggerService;
import com.simple.blog.backend.core.validator.CreatePostValidator;

import java.util.Set;
import java.util.stream.Collectors;

public class CreatePostUseCase implements ICreatePostUseCase {

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final ITagRepository tagRepository;
    private final ILoggerService loggerService;
    private final CreatePostValidator createPostValidator;

    public CreatePostUseCase(IPostRepository postRepository, IUserRepository userRepository, ITagRepository tagRepository, ILoggerService loggerService, CreatePostValidator createPostValidator) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.loggerService = loggerService;
        this.createPostValidator = createPostValidator;
    }

    @Override
    public CreatePostOutput execute(CreatePostInput createPostInput) {
        createPostValidator.validate(createPostInput);
        loggerService.info("[CREATE POST] ✔ validating input...");

        User postAuthor = this.userRepository.findById(createPostInput.userId());

        Set<TagType> tags = createPostInput.tagNames().stream()
                .map(TagType::fromString)
                .collect(Collectors.toSet());

        Set<Tag> postTags = this.tagRepository.findAllByTags(tags);
        loggerService.info("[CREATE POST] ✔ managing tags...");

        Post post = Post.create(
                createPostInput.title(),
                createPostInput.content(),
                createPostInput.imageUrl(),
                postAuthor,
                postTags
        );

        Post savedPost = this.postRepository.save(post);
        loggerService.info("[CREATE POST] ✔ saving new post for user ID: "+createPostInput.userId());

        return new CreatePostOutput(
                savedPost.getId(),
                postAuthor.getId(),
                postAuthor.getUsername(),
                savedPost.getCreatedAt(),
                savedPost.getTitle(),

                savedPost.getTags().stream()
                        .map(t -> t.getTag().name())
                        .collect(Collectors.toSet()),

                savedPost.getContent(),
                savedPost.getImageUrl());
    }
}
