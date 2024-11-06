package com.sanlam.easybank.publisher;

import com.sanlam.easybank.model.WithdrawalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Component
public class EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    private final SnsClient snsClient;

    private final String snsTopicArn;

    public EventPublisher(SnsClient snsClient, @Value("${aws.sns.topic.arn}") String snsTopicArn) {
        this.snsClient = snsClient;
        this.snsTopicArn = snsTopicArn;
    }

    public void publish(WithdrawalEvent event) {
        logger.info("Publishing withdrawal event for account {} to SNS: {}", event.getAccountId(), event);

        PublishRequest publishRequest = PublishRequest.builder()
                .message(event.toJson())
                .topicArn(snsTopicArn)
                .build();

        snsClient.publish(publishRequest);
        logger.info("Event published successfully for account {}", event.getAccountId());
    }
}
