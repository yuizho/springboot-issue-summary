package io.github.yuizho.springbootissuesummary.infrastructure.rest.jobs;

import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import io.github.yuizho.springbootissuesummary.infrastructure.DataSourceProperties;
import io.github.yuizho.springbootissuesummary.infrastructure.rest.adopters.IssuesRestFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IssuesRestCacheJob extends IssuesRestFetcher implements CacheJob {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    /**
     * this method is executed as scheduled job
     */
    @Override
    @Scheduled(fixedDelay = (1000 * 60 * 3))
    public void refreshCache() {
        if ("REST".equals(dataSourceProperties.getType())) {
            Cache issuesCache = cacheManager.getCache(DOMAIN_NAME);
            if (issuesCache == null) {
                return;
            }
            issuesCache.clear();
            Issues issues = fetchIssues();
            issuesCache.put(SimpleKey.EMPTY, issues);
        }
    }
}
