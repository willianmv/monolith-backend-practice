package com.simple.blog.backend.infra.controller;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.usecases.reply.create.CreateReplyOutput;
import com.simple.blog.backend.core.usecases.reply.create.ICreateReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.delete.IDeleteReplyUseCase;
import com.simple.blog.backend.core.usecases.reply.get.IGetRepliesUseCase;
import com.simple.blog.backend.core.usecases.reply.get.ReplySummaryOutput;
import com.simple.blog.backend.infra.config.security.SimpleBlogUserDetails;
import com.simple.blog.backend.infra.config.swagger.docs.ReplyControllerDoc;
import com.simple.blog.backend.infra.dto.input.CreateReplyDTO;
import com.simple.blog.backend.infra.dto.mapper.ReplyMapperDTO;
import com.simple.blog.backend.infra.dto.output.CreatedReplyDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/replies")
public class ReplyController implements ReplyControllerDoc {

    private final ICreateReplyUseCase createReplyUseCase;
    private final IGetRepliesUseCase getRepliesUseCase;
    private final IDeleteReplyUseCase deleteReplyUseCase;

    public ReplyController(ICreateReplyUseCase createReplyUseCase, IGetRepliesUseCase getRepliesUseCase, IDeleteReplyUseCase deleteReplyUseCase) {
        this.createReplyUseCase = createReplyUseCase;
        this.getRepliesUseCase = getRepliesUseCase;
        this.deleteReplyUseCase = deleteReplyUseCase;
    }

    @PostMapping
    public CreatedReplyDTO createReply(@AuthenticationPrincipal SimpleBlogUserDetails user, @Valid CreateReplyDTO input){
        CreateReplyOutput createdReply = createReplyUseCase.execute(ReplyMapperDTO.toCreateReplyInput(user, input));
        return ReplyMapperDTO.toCreatedReplyDTO(createdReply);
    }

    @GetMapping("/{postId}/replies")
    public ResponseEntity<DomainPage<ReplySummaryOutput>> getRepliesByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        DomainPage<ReplySummaryOutput> paginatedReplies = getRepliesUseCase.execute(postId, page, size);
        return ResponseEntity.ok(paginatedReplies);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId, @AuthenticationPrincipal SimpleBlogUserDetails user){
        deleteReplyUseCase.execute(user.getId(), replyId);
        return ResponseEntity.noContent().build();
    }

}
