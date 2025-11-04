package com.simple.blog.backend.core.event;

import com.simple.blog.backend.core.domain.User;

public record ActivatedAccountEvent(User user) {

}
