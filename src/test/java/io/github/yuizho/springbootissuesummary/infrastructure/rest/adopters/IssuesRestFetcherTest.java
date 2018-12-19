package io.github.yuizho.springbootissuesummary.infrastructure.rest.adopters;

import io.github.yuizho.springbootissuesummary.domain.exceptions.SystemException;
import io.github.yuizho.springbootissuesummary.infrastructure.rest.RestApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssuesRestFetcherTest {
    @Mock
    RestApiClient apiClient;

    @InjectMocks
    IssuesRestFetcher issuesRestFetcher;

    void setUpApiClientMock(JSONArray json) throws IOException, InterruptedException {
        when(apiClient.get(any(URI.class))).thenReturn(new HttpResponse<String>() {
            @Override
            public int statusCode() { return 0; }
            @Override
            public HttpRequest request() { return null; }
            @Override
            public Optional<HttpResponse<String>> previousResponse() { return Optional.empty(); }
            @Override
            public HttpHeaders headers() { return null; }
            @Override
            public Optional<SSLSession> sslSession() { return Optional.empty(); }
            @Override
            public URI uri() { return null; }
            @Override
            public HttpClient.Version version() { return null; }

            @Override
            public String body() {
                return json.toString();
            }
        });
    }

    @Test
    void normalResponseIsConvertedIntoIssues() throws IOException, InterruptedException {
        JSONObject json1 = new JSONObject()
                .put("title", "title1")
                .put("body", "body1");
        // this one is no body
        JSONObject json2 = new JSONObject()
                .put("title", "title2");
        // this one is no title
        JSONObject json3 = new JSONObject()
                .put("body", "body3");
        JSONArray respons = new JSONArray()
                .put(json1)
                .put(json2)
                .put(json3);
        setUpApiClientMock(respons);

        var actual = issuesRestFetcher.fetchIssues();
        assertThat(actual.getIssues())
                .hasSize(3)
                .extracting(i -> tuple(i.getTitle(), i.getBody()))
                .contains(tuple("title1", "body1"))
                .contains(tuple("title2", ""))
                .contains(tuple("", "body3"));
    }

    @Test
    void someExceptionIsThrownByApiClient() throws IOException, InterruptedException {
        when(apiClient.get(any(URI.class))).thenThrow(new IOException());
        assertThatThrownBy(() -> issuesRestFetcher.fetchIssues())
                .isInstanceOfSatisfying(SystemException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("Some kind of communication with external service was failed.");
                });
    }
}
