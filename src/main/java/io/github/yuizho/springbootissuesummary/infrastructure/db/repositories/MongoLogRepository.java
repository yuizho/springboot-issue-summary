package io.github.yuizho.springbootissuesummary.infrastructure.db.repositories;

import io.github.yuizho.springbootissuesummary.domain.models.LogData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoLogRepository extends MongoRepository<LogData, String> {
}
