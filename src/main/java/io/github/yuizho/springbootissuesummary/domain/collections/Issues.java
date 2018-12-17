package io.github.yuizho.springbootissuesummary.domain.collections;

import io.github.yuizho.springbootissuesummary.domain.exceptions.BusinessException;
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

    public List<Issue> asUnmodifiableList(int page, int perPage) {
        int listLength = issues.size();
        int head = (page - 1) * perPage;
        if (listLength <= head) {
            throw new BusinessException("the page size (start index) exceeds actual list length.");
        }
        int tail = page * perPage;
        if (listLength < tail) {
            tail = listLength;
        }
        return Collections.unmodifiableList(issues.subList(head, tail));
    }
}
