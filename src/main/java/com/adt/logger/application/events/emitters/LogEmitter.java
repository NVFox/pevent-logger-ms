package com.adt.logger.application.events.emitters;

import com.adt.logger.domain.entities.Log;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class LogEmitter {
    private final Sinks.Many<Log> sink = Sinks.many().multicast()
            .onBackpressureBuffer();

    public void emit(Log log) {
        sink.tryEmitNext(log);
    }

    public Flux<Log> asFlux() {
        return sink.asFlux();
    }
}
