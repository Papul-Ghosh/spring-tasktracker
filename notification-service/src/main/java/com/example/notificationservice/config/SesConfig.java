package com.example.notificationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class SesConfig {

    private final AwsCredentials creds;

    public SesConfig(AwsCredentials creds) {
        this.creds = creds;
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(Region.of(creds.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(creds.getAccessKeyId(), creds.getSecretKey())))
                .build();
    }
}