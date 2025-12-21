package com.simple.blog.backend.core.validator;

import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.gateway.ITagRepository;
import com.simple.blog.backend.core.gateway.IUserRepository;
import com.simple.blog.backend.core.usecases.post.create.CreatePostInput;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CreatePostValidator implements Validator<CreatePostInput> {

    private final IUserRepository userRepositoryGateway;
    private final ITagRepository tagRepositoryGateway;

    public CreatePostValidator(IUserRepository userRepositoryGateway, ITagRepository tagRepositoryGateway) {
        this.userRepositoryGateway = userRepositoryGateway;
        this.tagRepositoryGateway = tagRepositoryGateway;
    }

    @Override
    public void validate(CreatePostInput input) {
        if(input == null) throw new DomainException("Input cannot be null");
        if(input.title() == null || input.title().isBlank()) throw new DomainException("Title is required");
        if(input.content() == null || input.content().isBlank()) throw new DomainException("Content is required");
        if(input.userId() <= 0 ) throw new DomainException("Invalid user id");

        if(!userRepositoryGateway.existsById(input.userId())) throw new DomainException("User not found");

        Set<String> requestedTags = input.tagNames();
        Set<String> existingTags = tagRepositoryGateway.findAllTagNames();

        Set<String> invalidTags = requestedTags.stream()
                .filter(tag -> !existingTags.contains(tag.toUpperCase()))
                .collect(Collectors.toSet());

        if(!invalidTags.isEmpty()) throw new DomainException("Invalid tags: " + invalidTags);

    }
}
