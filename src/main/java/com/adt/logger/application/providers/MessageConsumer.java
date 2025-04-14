package com.adt.logger.application.providers;

import java.util.function.Consumer;

public interface MessageConsumer {
    <T> void consume(String queue, Consumer<T> consume);
}
