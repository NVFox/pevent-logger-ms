package com.adt.logger.infrastructure.messaging;

import com.adt.logger.application.events.emitters.LogEmitter;
import com.adt.logger.domain.entities.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageReceiver {
    private final LogEmitter logEmitter;

    @RabbitListener(queues = "${messaging.notifications-logs.queue-name}")
    public void log(Log log) {
        logEmitter.emit(log);
    }
}
