package com.simple.blog.backend.infra.config.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.SesClientBuilder;

import java.net.URI;

@Configuration
@Profile("prod")
public class AwsConfig {

    private final AwsProperties properties;

    public AwsConfig(AwsProperties properties) {
        this.properties = properties;
    }

    @Bean
    public StaticCredentialsProvider credentialsProvider(){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                properties.getAccessKey(),
                properties.getSecretKey()
        );
        return StaticCredentialsProvider.create(credentials);
    }

    @Bean
    public S3Client s3Client(){
        S3ClientBuilder s3ClientBuilder = S3Client.builder()
                .region(Region.of(properties.getS3().getRegion()))
                .credentialsProvider(credentialsProvider());

        if(properties.getLocalStack().getEndpoint() != null)
            s3ClientBuilder.endpointOverride(URI.create(properties.getLocalStack().getEndpoint()));

        s3ClientBuilder.serviceConfiguration(
                S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build()
        );

        return s3ClientBuilder.build();
    }

    @Bean
    public SesClient sesClient(){
        SesClientBuilder sesClientBuilder = SesClient.builder()
                .region(Region.of(properties.getSes().getRegion()))
                .credentialsProvider(credentialsProvider());

        if(properties.getLocalStack().getEndpoint() != null)
            sesClientBuilder.endpointOverride(URI.create(properties.getLocalStack().getEndpoint()));

        return sesClientBuilder.build();
    }

}
