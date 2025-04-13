package com.adt.logger.domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "logs")
@CompoundIndex(def = "{'level': 1, 'timestamp': -1}")
@Data
public class Log {
    @Id
    private String traceId;

    @Indexed
    private Level level;

    @TextIndexed
    private String message;

    @Indexed(direction = IndexDirection.DESCENDING)
    private LocalDateTime timestamp = LocalDateTime.now();

    public static Log info(String message) {
        Log log = new Log();

        log.setLevel(Level.INFO);
        log.setMessage(message);

        return log;
    }

    public static Log error(String message) {
        Log log = new Log();

        log.setLevel(Level.ERROR);
        log.setMessage(message);

        return log;
    }

    public enum Level {
        INFO,
        ERROR
    }
}
