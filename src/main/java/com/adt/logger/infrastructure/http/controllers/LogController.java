package com.adt.logger.infrastructure.http.controllers;

import com.adt.logger.application.dtos.LogQueryParamsDTO;
import com.adt.logger.application.queries.GetLogsByQuery;
import com.adt.logger.application.queries.handlers.GetLogsByQueryHandler;
import com.adt.logger.application.queries.handlers.GetRecentLogsQueryHandler;
import com.adt.logger.domain.entities.Log;
import com.adt.logger.domain.repositories.filters.LogFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogController {
    private final GetLogsByQueryHandler getLogsByQueryHandler;
    private final GetRecentLogsQueryHandler getRecentLogsQueryHandler;

    @GetMapping
    public Mono<Page<Log>> getLogsBy(@Valid LogQueryParamsDTO queryParams) {
        LogFilter filter = new LogFilter(
                queryParams.getTraceId(),
                queryParams.getLevel(),
                queryParams.getMessage(),
                queryParams.getFrom(),
                queryParams.getTo()
        );

        PageRequest pageRequest = PageRequest.of(
                queryParams.getPage(),
                queryParams.getSize()
        );

        return getLogsByQueryHandler.handle(new GetLogsByQuery(filter, pageRequest));
    }

    @GetMapping(value = "/recent", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Log> getMostRecentLogs() {
        return getRecentLogsQueryHandler.handle();
    }
}
