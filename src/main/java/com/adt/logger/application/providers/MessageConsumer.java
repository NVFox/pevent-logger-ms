package com.adt.logger.application.providers;

import java.util.function.Consumer;

public interface MessageConsumer {
    <T> void consume(String queue, Class<T> type, Consumer<T> consume);
}
