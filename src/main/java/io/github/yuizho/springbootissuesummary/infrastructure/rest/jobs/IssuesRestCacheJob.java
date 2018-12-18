package io.github.yuizho.springbootissuesummary.infrastructure.rest.jobs;

import io.github.yuizho.springbootissuesummary.domain.adopters.IssuesFetcher;
import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import io.github.yuizho.springbootissuesummary.infrastructure.DataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IssuesRestCacheJob implements CacheJob {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private Map<String, IssuesFetcher> issuesFetchers;

    private static final String DATA_SOURCE_TYPE_REST = "REST";

    /**
     * this method is executed as scheduled job
     */
    @Override
    @Scheduled(fixedDelay = (1000 * 60 * 3))
    public void refreshCache() {
        if (DATA_SOURCE_TYPE_REST.equals(dataSourceProperties.getType())) {
            Cache issuesCache = cacheManager.getCache(IssuesFetcher.DOMAIN_NAME);
            if (issuesCache == null) {
                return;
            }
            issuesCache.clear();
            // get IssuesRestFetcher
            Issues issues= issuesFetchers.get(IssuesFetcher.DOMAIN_NAME + DATA_SOURCE_TYPE_REST).fetchIssues();
            issuesCache.put(SimpleKey.EMPTY, issues);
        }
    }
}
