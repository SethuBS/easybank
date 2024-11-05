package com.sanlam.easybank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsConfig {

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.sns.topic.arn}")
    private String snsTopicArn;

    /**
     * Configures and provides an SNS client bean.
     *
     * @return SnsClient instance configured with credentials and region.
     */
    @Bean
    public SnsClient snsClient() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);

        return SnsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(region))
                .build();
    }

    /**
     * Provides the SNS Topic ARN.
     *
     * @return SNS Topic ARN as a String.
     */
    @Bean
    public String snsTopicArn() {
        return snsTopicArn;
    }
}
