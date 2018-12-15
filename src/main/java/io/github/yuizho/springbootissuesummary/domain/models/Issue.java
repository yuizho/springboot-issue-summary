package io.github.yuizho.springbootissuesummary.domain.models;

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
        return title.substring(0, 10);
    }

    public String getBody() {
        if (body == null) {
            return "";
        }
        // TODO: 多分バックスペースとか関連が中途半端に切られるとエラーになるっぽい
        return body;
    }
}
