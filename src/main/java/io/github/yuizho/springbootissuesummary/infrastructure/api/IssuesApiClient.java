package io.github.yuizho.springbootissuesummary.infrastructure.api;

import io.github.yuizho.springbootissuesummary.domain.adopters.IssuesFetcher;
import io.github.yuizho.springbootissuesummary.domain.models.Issue;
import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component("IssuesAPI")
public class IssuesApiClient extends AbstractApiClient implements IssuesFetcher {
    private static final String uri = "https://api.github.com/repos/spring-projects/spring-boot/issues?page=1&per_page=10";

    @Cacheable(value = "issues")
    @Override
    public Issues fetchIssues() {
        HttpResponse<String> response = fetch();
        return convertResultToDomain(response.body());
    }

    // TODO: ここで有効期限指定したい。。。Taskとかで定期的に消さないとだめかな？
    @CachePut(value = "issues")
    Issues convertResultToDomain(String body) {
        JSONArray jsonArray = new JSONArray(body);

        List<Issue> issues = new ArrayList<>();
        jsonArray.forEach(item -> {
            JSONObject jsonObj = (JSONObject) item;
            issues.add(new Issue(jsonObj.getString("title"),
                    jsonObj.getString("body")));
        });
        return new Issues(issues);
    }

    @Override
    URI getURI() {
        return URI.create(uri);
    }
}
