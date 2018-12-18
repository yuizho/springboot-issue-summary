package io.github.yuizho.springbootissuesummary.infrastructure.rest.adopters;

import io.github.yuizho.springbootissuesummary.domain.adopters.IssuesFetcher;
import io.github.yuizho.springbootissuesummary.domain.exceptions.SystemException;
import io.github.yuizho.springbootissuesummary.domain.models.Issue;
import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import io.github.yuizho.springbootissuesummary.infrastructure.rest.RestApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component("IssuesREST")
public class IssuesRestFetcher implements IssuesFetcher {
    private static final String ISSUES_URI
            = "https://api.github.com/repos/spring-projects/spring-boot/issues?page=1&per_page=100";

    @Autowired
    private RestApiClient apiClient;

    @Cacheable(value = DOMAIN_NAME)
    @Override
    public Issues fetchIssues() {
        try {
            HttpResponse<String> response = apiClient.get(URI.create(ISSUES_URI));
            return convertResultToDomain(response.body());
        } catch (IOException | InterruptedException ex) {
            throw new SystemException("Some kind of communication with external service was failed.", ex);
        }
    }

    Issues convertResultToDomain(String body) {
        JSONArray jsonArray = new JSONArray(body);
        List<Issue> issues = new ArrayList<>();
        jsonArray.forEach(item -> {
            JSONObject jsonObj = (JSONObject) item;
            issues.add(new Issue(jsonObj.optString("title"),
                    jsonObj.optString("body")));
        });
        return new Issues(issues);
    }
}
