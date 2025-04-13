package com.adt.logger.application.services;

import com.adt.logger.domain.entities.Log;
import com.adt.logger.domain.repositories.LogRepository;
import com.adt.logger.domain.repositories.filters.LogFilter;
import com.adt.logger.domain.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;

    @Override
    public Mono<Log> create(Log log) {
        return logRepository.save(log);
    }

    @Override
    public Mono<Page<Log>> findBy(LogFilter filter, Pageable pageable) {
        return logRepository.findBy(filter, pageable);
    }

    @Override
    public Flux<Log> findMostRecent() {
        return logRepository.findMostRecent();
    }
}
