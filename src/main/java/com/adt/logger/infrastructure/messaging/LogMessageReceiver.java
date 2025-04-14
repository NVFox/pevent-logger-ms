package com.adt.logger.infrastructure.messaging;

import com.adt.logger.application.commands.CreateLogCommand;
import com.adt.logger.application.commands.handlers.CreateLogCommandHandler;
import com.adt.logger.application.providers.MessageConsumer;
import com.adt.logger.domain.entities.Log;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogMessageReceiver {
    private final MessageConsumer messageConsumer;
    private final CreateLogCommandHandler createLogCommandHandler;

    @Value("${messaging.notifications-logs.queue-name}")
    private String logQueue;

    @PostConstruct
    public void init() {
        messageConsumer.consume(logQueue, this::log);
    }

    public void log(Log log) {
        createLogCommandHandler.handle(new CreateLogCommand(log));
    }
}
