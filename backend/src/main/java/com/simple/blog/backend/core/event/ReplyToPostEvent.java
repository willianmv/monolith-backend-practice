package com.simple.blog.backend.core.event;

import com.simple.blog.backend.core.domain.User;

public record ReplyToPostEvent(User postAuthor, User replyAuthor, String postTitle) {
}
