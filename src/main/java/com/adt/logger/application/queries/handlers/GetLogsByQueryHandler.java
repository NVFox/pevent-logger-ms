package com.adt.logger.application.queries.handlers;

import com.adt.logger.application.queries.GetLogsByQuery;
import com.adt.logger.domain.entities.Log;
import com.adt.logger.domain.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class GetLogsByQueryHandler {
    private final LogService logService;

    public Mono<Page<Log>> handle(GetLogsByQuery query) {
        return logService.create(Log.info("** Starting logs query with filters: " + query.filter() + " **"))
                .then(logService.findBy(query.filter(), query.pageable()))
                .publishOn(Schedulers.boundedElastic())
                .doOnError(error ->
                        log(Log.error("Error querying logs: " + error.getMessage())))
                .doOnSuccess(saved ->
                        log(Log.info("Logs retrieved successfully (" + saved.getTotalElements() + ")")))
                .doFinally(signalType ->
                        log(Log.info("** Log query completed with signal " + signalType + " **")));
    }

    private void log(Log log) {
        logService.create(log)
                .retry(3)
                .subscribe();
    }
}
