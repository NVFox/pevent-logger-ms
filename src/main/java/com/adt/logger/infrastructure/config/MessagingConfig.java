package com.adt.logger.infrastructure.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import java.io.IOException;
import java.util.Objects;

@Configuration
public class MessagingConfig {
    @Autowired
    private Mono<Connection> connectionMono;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${messaging.notifications.topic-name}")
    private String notificationTopic;

    @Value("${messaging.notifications-logs.queue-name}")
    private String logQueue;

    @Value("${messaging.notifications-logs.channel-name}")
    private String logChannel;

    @Bean
    public Mono<Connection> connectionMono(RabbitProperties rabbitProperties) {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.useNio();

        return Mono.fromCallable(() -> connectionFactory
                .newConnection("reactive-logger-rabbitmq")).cache();
    }

    @Bean
    public ReceiverOptions receiverOptions(Mono<Connection> connectionMono) {
        return new ReceiverOptions()
                .connectionMono(connectionMono);
    }

    @Bean
    public Receiver receiver(ReceiverOptions receiverOptions) {
        return RabbitFlux.createReceiver(receiverOptions);
    }

    private Exchange notificationsExchange() {
        return new TopicExchange(notificationTopic);
    }

    private Queue logQueue() {
        return new Queue(logQueue, true);
    }

    private Binding logBinding() {
        return BindingBuilder.bind(logQueue())
                .to(notificationsExchange())
                .with(logChannel)
                .noargs();
    }

    @PostConstruct
    public void init() {
        amqpAdmin.declareExchange(notificationsExchange());
        amqpAdmin.declareQueue(logQueue());
        amqpAdmin.declareBinding(logBinding());
    }

    @PreDestroy
    public void close() throws IOException {
        Objects.requireNonNull(connectionMono.block()).close();
    }
}
