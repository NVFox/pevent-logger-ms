package com.adt.logger.domain.services;

import com.adt.logger.domain.entities.Log;
import com.adt.logger.domain.repositories.filters.LogFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LogService {
    Mono<Log> create(Log log);
    Mono<Page<Log>> findBy(LogFilter filter, Pageable pageable);
    Flux<Log> findMostRecent();
}
