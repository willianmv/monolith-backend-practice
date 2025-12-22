package com.simple.blog.backend.infra.service.email;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class LocalEmailService implements IEmailService {

    private final JavaMailSender javaMailSender;

    public LocalEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(EmailMessage emailMessage) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.to());
        simpleMailMessage.setSubject(emailMessage.subject());
        simpleMailMessage.setText(emailMessage.body());
        simpleMailMessage.setFrom("noreply@simpleblog.com");

        javaMailSender.send(simpleMailMessage);
    }
}
