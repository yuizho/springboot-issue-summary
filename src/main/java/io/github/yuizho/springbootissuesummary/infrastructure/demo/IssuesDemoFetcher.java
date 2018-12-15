package io.github.yuizho.springbootissuesummary.infrastructure.demo;

import io.github.yuizho.springbootissuesummary.domain.adopters.IssuesFetcher;
import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import io.github.yuizho.springbootissuesummary.domain.models.Issue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("IssuesDEMO")
public class IssuesDemoFetcher implements IssuesFetcher {
    @Override
    public Issues fetchIssues() {
        return new Issues(List.of(new Issue("title", "body")));
    }
}
