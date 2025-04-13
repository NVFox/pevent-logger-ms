package com.adt.logger.domain.repositories.filters;

import com.adt.logger.domain.entities.Log;

import java.time.LocalDateTime;

public record LogFilter(
        String traceId,
        Log.Level level,
        String message,
        LocalDateTime from,
        LocalDateTime to
) {
}
