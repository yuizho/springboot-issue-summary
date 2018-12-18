package io.github.yuizho.springbootissuesummary.domain.collections;

import io.github.yuizho.springbootissuesummary.domain.exceptions.SystemException;
import io.github.yuizho.springbootissuesummary.domain.models.Issue;

import java.util.*;

public class Issues {
    private final List<Issue> issues;

    // TOOD: propertiesが良いかも。
    private static final int DEFAULT_PER_PAGE = 10;

    public Issues() {
        this.issues = new ArrayList<>();
    }

    public Issues(List<Issue> issues) {
        if (issues == null) {
            this.issues = new ArrayList<>();
            return;
        }
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

    public List<Issue> asUnmodifiableList(OptionalInt optPage, OptionalInt optPerPage) {
        int page = optPage.orElse(1);
        int perPage = optPerPage.orElse(DEFAULT_PER_PAGE);
        if (page <= 0 || perPage <= 0) {
            throw new SystemException("0 or minus value was set to pagination parameter. probably some validation of controller is not enough.");
        }
        int listLength = issues.size();

        int head = (page - 1) * perPage;
        if (listLength <= head) {
            return Collections.unmodifiableList(new ArrayList<>());
        }

        int tail = page * perPage;
        if (listLength < tail) {
            tail = listLength;
        }
        return Collections.unmodifiableList(issues.subList(head, tail));
    }
}
