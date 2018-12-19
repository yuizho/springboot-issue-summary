package io.github.yuizho.springbootissuesummary.domain.collections;

import io.github.yuizho.springbootissuesummary.domain.exceptions.SystemException;
import io.github.yuizho.springbootissuesummary.domain.models.Issue;

import java.util.*;

public class Issues {

    private final List<Issue> issues;

    private final boolean hasNext;

    private static final int DEFAULT_PER_PAGE = 10;

    public Issues() {
        this(new ArrayList<>());
    }

    public Issues(List<Issue> issues) {
        this(issues, false);
    }

    public Issues(List<Issue> issues, boolean hasNext) {
        if (issues == null) {
            this.issues = new ArrayList<>();
            this.hasNext = false;
            return;
        }
        this.issues = issues;
        this.hasNext = hasNext;
    }

    public Issues add(Issue issue) {
        List<Issue> tmpIssues = new ArrayList<>(issues);
        tmpIssues.add(issue);
        return new Issues(tmpIssues);
    }

    public List<Issue> getIssues() {
        return Collections.unmodifiableList(issues);
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public Issues asPaginated(Optional<Integer> optPage, Optional<Integer> optPerPage) {
        int page = optPage.orElse(1);
        int perPage = optPerPage.orElse(DEFAULT_PER_PAGE);
        if (page <= 0 || perPage <= 0) {
            throw new SystemException("0 or minus value was set to pagination parameter. probably some validation of controller is not enough.");
        }
        int listLength = issues.size();

        int head = computeHeadIndex(page, perPage);
        if (listLength <= head) {
            return new Issues(new ArrayList<>());
        }
        int tail = computeTailIndex(page, perPage, listLength);

        return new Issues(issues.subList(head, tail), tail < listLength);
    }

    private int computeHeadIndex(int page, int perPage) {
        return (page - 1) * perPage;
    }

    private int computeTailIndex(int page, int perPage, int listLength) {
        int tail = page * perPage;
        if (listLength < tail) {
            tail = listLength;
        }
        return tail;
    }
}
