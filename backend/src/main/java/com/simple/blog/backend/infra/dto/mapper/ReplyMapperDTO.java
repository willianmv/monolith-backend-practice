package com.simple.blog.backend.infra.dto.mapper;

import com.simple.blog.backend.core.usecases.reply.create.CreateReplyInput;
import com.simple.blog.backend.core.usecases.reply.create.CreateReplyOutput;
import com.simple.blog.backend.infra.config.security.SimpleBlogUserDetails;
import com.simple.blog.backend.infra.dto.input.CreateReplyDTO;
import com.simple.blog.backend.infra.dto.output.CreatedReplyDTO;

public class ReplyMapperDTO {

    public static CreateReplyInput toCreateReplyInput(SimpleBlogUserDetails author, CreateReplyDTO input){
        return new CreateReplyInput(author.getId(), input.postId(), input.content());
    }

    public static CreatedReplyDTO toCreatedReplyDTO(CreateReplyOutput output){
        return new CreatedReplyDTO(
                output.replyAuthor(),
                output.repliedTo(),
                output.content()
        );
    }

}
