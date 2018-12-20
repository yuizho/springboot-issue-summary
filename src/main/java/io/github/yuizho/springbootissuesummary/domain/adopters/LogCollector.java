package io.github.yuizho.springbootissuesummary.domain.adopters;

import io.github.yuizho.springbootissuesummary.domain.models.LogData;

public interface LogCollector {
    void collect(LogData logData);
}
