package com.adt.logger.infrastructure.events.listeners;

import com.adt.logger.application.events.emitters.LogEmitter;
import com.adt.logger.domain.entities.Log;
import com.adt.logger.domain.services.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LogListener {
    private final LogService logService;

    public LogListener(LogEmitter logEmitter, LogService logService) {
        this.logService = logService;

        logEmitter.asFlux()
                .flatMap(this::handle)
                .subscribe();
    }

    public Mono<Log> handle(Log log) {
        return logService.create(log)
                .doOnSuccess(saved -> LogListener.log.info("Log created: {}", saved))
                .doOnError(error -> LogListener.log.error("Error creating log: {}", log, error))
                .onErrorResume(e -> Mono.empty());
    }
}
