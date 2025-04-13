package com.adt.logger.application.queries;

import com.adt.logger.domain.repositories.filters.LogFilter;
import org.springframework.data.domain.Pageable;

public record GetLogsByQuery(
        LogFilter filter,
        Pageable pageable
) {
}
