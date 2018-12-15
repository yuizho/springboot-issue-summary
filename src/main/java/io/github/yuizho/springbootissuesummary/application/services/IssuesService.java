package io.github.yuizho.springbootissuesummary.application.services;

import io.github.yuizho.springbootissuesummary.domain.adopters.IssuesFetcher;
import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IssuesService {
    @Autowired
    private IssuesFetcher issuesFetcher;

    // TODO: キャッシュから取るようにする
    public Issues fetchIssues() {
        return issuesFetcher.fetchIssues();
    }
}
