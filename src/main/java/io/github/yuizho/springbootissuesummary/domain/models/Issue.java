package io.github.yuizho.springbootissuesummary.domain.models;

import org.apache.commons.lang3.StringUtils;

public class Issue {
    private final String title;
    private final String body;

    public Issue(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }
        return StringUtils.substring(title, 0, 10);
    }

    public String getBody() {
        if (body == null) {
            return "";
        }
        return StringUtils.substring(body, 0, 30);
    }
}
