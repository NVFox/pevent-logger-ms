package com.adt.logger.infrastructure.config;

import com.adt.logger.application.constants.MessagingConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class MessagingConfig {
    @Bean
    public Queue eventQueue(@Value("${messaging.notifications-events.queue-name}") String eventQueue) {
        return new Queue(eventQueue, false);
    }

    @Bean
    public Exchange notificationsExchange(@Value("${messaging.notifications.topic-name}") String notificationTopic) {
        return new TopicExchange(notificationTopic);
    }

    @Bean
    public Binding eventBinding(Queue eventQueue, Exchange notificationsExchange) {
        return BindingBuilder.bind(eventQueue)
                .to(notificationsExchange)
                .with(MessagingConstants.EVENTS_CHANNEL_NAME)
                .noargs();
    }
}
