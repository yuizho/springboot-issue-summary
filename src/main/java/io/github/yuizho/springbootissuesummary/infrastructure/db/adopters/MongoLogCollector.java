package io.github.yuizho.springbootissuesummary.infrastructure.db.adopters;

import io.github.yuizho.springbootissuesummary.domain.adopters.LogCollector;
import io.github.yuizho.springbootissuesummary.domain.models.LogData;
import io.github.yuizho.springbootissuesummary.infrastructure.db.repositories.MongoLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("MongoLogCollector")
public class MongoLogCollector implements LogCollector {
    @Autowired
    private MongoLogRepository mongoLogRepository;

    @Override
    public void collect(LogData logData) {
        mongoLogRepository.save(logData);
    }
}
