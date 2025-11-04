package com.simple.blog.backend.infra.service.email;

import com.simple.blog.backend.core.domain.User;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EmailMessageFactory {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.of("America/Sao_Paulo"));

    public static EmailMessage buildAccountActivation(User user, String code, Instant expiresAt){

        String formattedExpiresDate = dateTimeFormatter.format(expiresAt);

        String subject = "Activate your SimpleBlog account";

        String body = "Hello " + user.getName() + ",\n\n" +
                "Use the following code to activate your account:\n\n" +
                 code + "\n\n" +
                "This code is going to be expired at - " + formattedExpiresDate + "\n\n" +
                "Thank you,\n\nSimpleBlog Team";

        return new EmailMessage(user.getEmail(), subject, body);
    }

    public static EmailMessage buildWelcome(User user){
        String subject = "Welcome to SimpleBlog";

        String body = "Hello " + user.getName() + ",\n\n" +
                "Your account has been successfully activated.\n\n" +
                "We're glad to have you on board!\n\n" +
                "Enjoy writing and connecting.\n\n" +
                "SimpleBlog Team";

        return new EmailMessage(user.getEmail(), subject, body);
    }

    public static EmailMessage buildReplyToPost(User postAuthor, User replyAuthor, String postTitle){
        String subject = "New reply!";

        String body = "Hello " + postAuthor.getUsername()+ ",\n\n" +
                "Your post: \""+postTitle+"\" \n\n" +
                "Just got a reply from: "+ replyAuthor.getUsername() +"\n\n" +
                "Enjoy writing and connecting.\n\n" +
                "SimpleBlog Team";

        return new EmailMessage(postAuthor.getEmail(), subject, body);
    }

}
