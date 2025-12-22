package com.simple.blog.backend.infra.controller;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.usecases.post.create.CreatePostInput;
import com.simple.blog.backend.core.usecases.post.create.CreatePostOutput;
import com.simple.blog.backend.core.usecases.post.create.ICreatePostUseCase;
import com.simple.blog.backend.core.usecases.post.delete.IDeletePostUseCase;
import com.simple.blog.backend.core.usecases.post.detailed.DetailedPostOutput;
import com.simple.blog.backend.core.usecases.post.detailed.IGetPostUseCase;
import com.simple.blog.backend.core.usecases.post.summary.IGetPostsUseCase;
import com.simple.blog.backend.core.usecases.post.summary.PostQueryFilter;
import com.simple.blog.backend.core.usecases.post.summary.SummaryPostOutput;
import com.simple.blog.backend.infra.config.security.SimpleBlogUserDetails;
import com.simple.blog.backend.infra.config.swagger.docs.PostControllerDoc;
import com.simple.blog.backend.infra.dto.mapper.PostMapperDTO;
import com.simple.blog.backend.infra.dto.output.CreatedPostDTO;
import com.simple.blog.backend.infra.service.image.IImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/posts")
public class PostController implements PostControllerDoc {

    private final IImageService imageStorageService;
    private final ICreatePostUseCase createPostUseCase;
    private final IGetPostsUseCase getPostsUseCase;
    private final IGetPostUseCase getPostUseCase;
    private final IDeletePostUseCase deletePostUseCase;

    public PostController(IImageService imageStorageService, ICreatePostUseCase createPostUseCase, IGetPostsUseCase getPostsUseCase, IGetPostUseCase getPostUseCase, IDeletePostUseCase deletePostUseCase) {
        this.imageStorageService = imageStorageService;
        this.createPostUseCase = createPostUseCase;
        this.getPostsUseCase = getPostsUseCase;
        this.getPostUseCase = getPostUseCase;
        this.deletePostUseCase = deletePostUseCase;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreatedPostDTO> createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Set<String> tagNames,
            @RequestPart(required = false)MultipartFile image,
            @AuthenticationPrincipal SimpleBlogUserDetails user){

        String imageUrl = handleImage(image);
        CreatePostOutput createdPost = createPostUseCase.execute(
                new CreatePostInput(user.getId(), title, content, imageUrl, tagNames));

        return ResponseEntity.ok().body(PostMapperDTO.toCreatedPostDTO(createdPost));
    }

    @GetMapping()
    public ResponseEntity<DomainPage<SummaryPostOutput>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Set<String> tags,
            @RequestParam(required = false) Long authorId,
            @RequestParam(defaultValue = "NEWEST")PostQueryFilter.SortOrder sortOrder){

        PostQueryFilter filter = new PostQueryFilter(title, tags, sortOrder, authorId);
        return ResponseEntity.ok(getPostsUseCase.execute(filter, page, size));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<DetailedPostOutput> getPost(@PathVariable Long postId){
        DetailedPostOutput detailedPost = getPostUseCase.execute(postId);
        return ResponseEntity.ok(detailedPost);
    }

    @DeleteMapping("/{postId}")
    public  ResponseEntity<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal SimpleBlogUserDetails user){
        deletePostUseCase.execute(postId, user.getId());
        return ResponseEntity.noContent().build();
    }


    private String handleImage(MultipartFile image){
        String imageUrl = "";
        if(image != null && !image.isEmpty()) {
            imageUrl = imageStorageService.save(image);
        }
        return imageUrl;
    }

}
