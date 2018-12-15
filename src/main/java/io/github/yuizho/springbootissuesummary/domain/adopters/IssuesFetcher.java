package io.github.yuizho.springbootissuesummary.domain.adopters;

import io.github.yuizho.springbootissuesummary.domain.collections.Issues;

public interface IssuesFetcher {
    Issues fetchIssues();
}
