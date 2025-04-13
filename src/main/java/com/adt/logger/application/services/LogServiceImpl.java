package com.adt.logger.application.services;

import com.adt.logger.domain.entities.Log;
import com.adt.logger.domain.repositories.LogRepository;
import com.adt.logger.domain.repositories.filters.LogFilter;
import com.adt.logger.domain.services.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;

    @Override
    public Mono<Log> create(Log log) {
        return logRepository.save(log)
                .doOnSuccess(saved -> LogServiceImpl.log.info("Log created: {}", log))
                .doOnError(error -> LogServiceImpl.log.error("Error creating log: {}", log, error));
    }

    @Override
    public Mono<Page<Log>> findBy(LogFilter filter, Pageable pageable) {
        return logRepository.findBy(filter, pageable);
    }
}
