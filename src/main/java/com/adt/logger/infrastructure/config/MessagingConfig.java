package com.adt.logger.infrastructure.config;

import com.adt.logger.application.constants.MessagingConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class MessagingConfig {
    @Bean
    public Queue logQueue(@Value("${messaging.notifications-logs.queue-name}") String logQueue) {
        return new Queue(logQueue, false);
    }

    @Bean
    public Exchange notificationsExchange(@Value("${messaging.notifications.topic-name}") String notificationTopic) {
        return new TopicExchange(notificationTopic);
    }

    @Bean
    public Binding eventBinding(Queue logQueue, Exchange notificationsExchange) {
        return BindingBuilder.bind(logQueue)
                .to(notificationsExchange)
                .with(MessagingConstants.EVENTS_CHANNEL_NAME)
                .noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter converter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
