package com.simple.blog.backend.infra.service.email;

import com.simple.blog.backend.infra.config.aws.AwsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
@Profile("prod")
public class SesEmailService implements IEmailService{

    private static final Logger log = LoggerFactory.getLogger(SesEmailService.class);
    private final SesClient sesClient;
    private final String fromEmail;


    public SesEmailService(SesClient sesClient, AwsProperties awsProperties) {
        this.sesClient = sesClient;
        this.fromEmail = awsProperties.getSes().getFromEmail();
    }

    @Override
    public void send(EmailMessage emailMessage) {
        log.info("[SES SERVICE] - Enviando email para: {}", emailMessage.to());

        SendEmailRequest request = SendEmailRequest.builder()
                .source(fromEmail)
                .destination(
                        Destination.builder()
                                .toAddresses(emailMessage.to())
                                .build())
                .message(
                        Message.builder()
                            .subject(Content.builder().data(emailMessage.subject()).build())
                            .body(Body.builder().text(Content.builder().data(emailMessage.body()).build()).build())
                        .build())
                .build();

        sesClient.sendEmail(request);

        log.info("[SES SERVICE] - Email enviado para: {}", emailMessage.to());

    }
}
