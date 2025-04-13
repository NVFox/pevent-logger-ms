package com.adt.logger.application.queries.handlers;

import com.adt.logger.application.events.emitters.LogEmitter;
import com.adt.logger.domain.entities.Log;
import com.adt.logger.domain.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetRecentLogsQueryHandler {
    private final LogService logService;
    private final LogEmitter logEmitter;

    public Flux<Log> handle() {
        logEmitter.emit(Log.info("** Starting recent logs query **"));

        return logService.findMostRecent()
                .doOnError(error ->
                        logEmitter.emit(Log.error("Error querying recent logs: " + error.getMessage())))
                .doOnComplete(() ->
                        logEmitter.emit(Log.info("Recent logs retrieved successfully")))
                .doFinally(signalType ->
                        logEmitter.emit(Log.info("** Recent logs query completed with signal " + signalType + " **")));
    }
}
