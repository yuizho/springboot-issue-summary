package io.github.yuizho.springbootissuesummary.domain.collections;

import io.github.yuizho.springbootissuesummary.domain.models.Issue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Issues {
    private final List<Issue> issues;

    public Issues() {
        this.issues = new ArrayList<>();
    }

    public Issues(List<Issue> issues) {
        this.issues = issues;
    }

    public Issues add(Issue issue) {
        List<Issue> tmpIssues = new ArrayList<>(issues);
        tmpIssues.add(issue);
        return new Issues(tmpIssues);
    }

    public List<Issue> asUnmodifiableList() {
        return Collections.unmodifiableList(issues);
    }
}
