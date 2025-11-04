package com.simple.blog.backend.infra.service.email;

import com.simple.blog.backend.core.event.AccountActivationEvent;
import com.simple.blog.backend.core.event.ActivatedAccountEvent;
import com.simple.blog.backend.core.event.ReplyToPostEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailEventListener {

    private final IEmailService emailService;

    public EmailEventListener(IEmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handle(AccountActivationEvent event){
        EmailMessage emailMessage = EmailMessageFactory.buildAccountActivation(event.user(), event.code(), event.expiresAt());
        emailService.send(emailMessage);
    }

    @EventListener
    public void handle(ActivatedAccountEvent event){
        EmailMessage emailMessage = EmailMessageFactory.buildWelcome(event.user());
        emailService.send(emailMessage);
    }

    @EventListener
    public void handle(ReplyToPostEvent event){
        EmailMessage emailMessage = EmailMessageFactory.buildReplyToPost(event.postAuthor(), event.replyAuthor(),event.postTitle());
        emailService.send(emailMessage);
    }
}
