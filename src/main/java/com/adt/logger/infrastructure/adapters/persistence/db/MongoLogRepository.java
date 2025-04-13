package com.adt.logger.infrastructure.adapters.persistence.db;

import com.adt.logger.domain.entities.Log;
import com.adt.logger.domain.repositories.LogRepository;
import com.adt.logger.domain.repositories.filters.LogFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MongoLogRepository implements LogRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    private static final String TIMESTAMP_FIELD = "timestamp";

    @Override
    public Mono<Log> save(Log log) {
        return mongoTemplate.save(log);
    }

    @Override
    public Mono<Page<Log>> findBy(LogFilter filter, Pageable pageable) {
        Criteria criteria = new Criteria();

        if (filter.traceId() != null)
            criteria = criteria.and("traceId").is(filter.traceId());

        if (filter.level() != null)
            criteria = criteria.and("level").is(filter.level());

        if (filter.from() != null)
            criteria = criteria.and(TIMESTAMP_FIELD).gte(filter.from());

        if (filter.to() != null)
            criteria = criteria.and(TIMESTAMP_FIELD).lte(filter.to());

        Query query = new Query(criteria)
                .with(pageable);

        if (filter.message() != null) {
            TextCriteria messageCriteria = TextCriteria.forDefaultLanguage()
                    .matching(filter.message());

            query = query.addCriteria(messageCriteria);
        }

        return mongoTemplate.find(query, Log.class)
                .collectList()
                .zipWith(mongoTemplate.count(query, Log.class),
                        (logs, count) -> new PageImpl<>(logs, pageable, count));
    }

    @Override
    public Flux<Log> findMostRecent() {
        return mongoTemplate.tail(new Query(), Log.class);
    }
}
