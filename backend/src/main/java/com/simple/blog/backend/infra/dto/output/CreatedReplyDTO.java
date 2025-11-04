package com.simple.blog.backend.infra.dto.output;

public record CreatedReplyDTO(
        String replyAuthor,
        String repliedTo,
        String content) {}
