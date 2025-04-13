package com.adt.logger.application.providers;

public interface MessagingProvider {
    void publish(String topic, String key, Object message);
}