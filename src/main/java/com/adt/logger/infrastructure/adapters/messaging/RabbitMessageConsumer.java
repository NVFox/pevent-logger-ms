package com.adt.logger.infrastructure.adapters.messaging;

import com.adt.logger.application.providers.MessageConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.SerializationUtils;
import reactor.rabbitmq.Receiver;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class RabbitMessageConsumer implements MessageConsumer {
    private final Receiver receiver;
    private final ObjectMapper objectMapper;

    public <T> void consume(String queue, Consumer<T> consume) {
        receiver.consumeAutoAck(queue)
                .subscribe(convertToTypeAndThen(consume));
    }

    public <T> Consumer<Delivery> convertToTypeAndThen(Consumer<T> consume) {
        return delivery -> {
            String json = SerializationUtils.deserialize(delivery.getBody());

            try {
                T message = objectMapper.readValue(json, new TypeReference<>(){});
                consume.accept(message);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error deserializing message", e);
            }
        };
    }
}
