package io.github.yuizho.springbootissuesummary.infrastructure.std.adopters;

import io.github.yuizho.springbootissuesummary.domain.adopters.LogCollector;
import io.github.yuizho.springbootissuesummary.domain.models.LogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("StdLogCollector")
public class StdLogCollector implements LogCollector {
    private final Logger logger = LoggerFactory.getLogger(StdLogCollector.class);

    @Override
    public void collect(LogData logData) {
        logger.info(logData.getLogText());
    }
}
