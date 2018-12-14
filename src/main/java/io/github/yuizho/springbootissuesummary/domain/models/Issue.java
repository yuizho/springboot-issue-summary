package io.github.yuizho.springbootissuesummary.domain.models;

import lombok.Getter;

@Getter
public class Issue {
    // TODO: 値オブジェクトへの切り出し
    private final String title;
    private final String body;

    // TODO: 要検討
    public Issue(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
