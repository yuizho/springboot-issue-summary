package io.github.yuizho.springbootissuesummary.domain.models;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class LogData {
    @Id
    public String id;
    private final Date logDateTime;
    private final String logText;

    public LogData (String logText) {
        this.logDateTime = new Date();
        this.logText = logText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLogDateTime() {
        return logDateTime;
    }

    public String getLogText() {
        return logText;
    }
}
