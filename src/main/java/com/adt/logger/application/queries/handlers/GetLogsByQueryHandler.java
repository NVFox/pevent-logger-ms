package com.adt.logger.application.queries.handlers;

import com.adt.logger.application.events.emitters.LogEmitter;
import com.adt.logger.application.queries.GetLogsByQuery;
import com.adt.logger.domain.entities.Log;
import com.adt.logger.domain.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetLogsByQueryHandler {
    private final LogService logService;
    private final LogEmitter logEmitter;

    public Mono<Page<Log>> handle(GetLogsByQuery query) {
        logEmitter.emit(Log.info("** Starting logs query with filters: " + query.filter() + " **"));

        return logService.findBy(query.filter(), query.pageable())
                .doOnError(error ->
                        logEmitter.emit(Log.error("Error querying logs: " + error.getMessage())))
                .doOnSuccess(saved ->
                        logEmitter.emit(Log.info("Logs retrieved successfully (" + saved.getTotalElements() + ")")))
                .doFinally(signalType ->
                        logEmitter.emit(Log.info("** Log query completed with signal " + signalType + " **")));
    }
}
