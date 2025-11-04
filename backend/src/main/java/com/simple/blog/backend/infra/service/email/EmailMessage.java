package com.simple.blog.backend.infra.service.email;

public record EmailMessage(String to, String subject, String body) {

}
