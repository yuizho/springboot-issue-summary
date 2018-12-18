package io.github.yuizho.springbootissuesummary.application.services;

import io.github.yuizho.springbootissuesummary.infrastructure.DataSourceProperties;
import io.github.yuizho.springbootissuesummary.domain.adopters.IssuesFetcher;
import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service()
public class IssuesService {
    // I referenced this page to inject with condition.
    // https://qiita.com/gagagaga_dev/items/c16e5b6b3dff6df7e406
    @Autowired
    private Map<String, IssuesFetcher> issuesFetchers;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    public Issues fetchIssues(Optional<Integer> optPage, Optional<Integer> optPerPage) {
        String targetComponentName
                = IssuesFetcher.DOMAIN_NAME + dataSourceProperties.getType();
        return issuesFetchers.get(targetComponentName).fetchIssues().asPaginated(optPage, optPerPage);
    }
}
