package io.github.yuizho.springbootissuesummary.infrastructure.rest;

import io.github.yuizho.springbootissuesummary.domain.adopters.LogCollector;
import io.github.yuizho.springbootissuesummary.infrastructure.LogCollectorProperties;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@RunWith(MockitoJUnitRunner.class)
public class RestApiClientTest {
    @Mock
    private Map<String, LogCollector> logCollectors;

    @Mock
    private LogCollectorProperties logCollectorProperties;

    @InjectMocks
    private RestApiClient restApiClient;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 1081);

    private MockServerClient mockServerClient;

    @Before
    public void setUp() {
        when(logCollectorProperties.getName()).thenReturn("MockLogCollector");
        when(logCollectors.get(anyString())).thenReturn(mock(LogCollector.class));
    }

    @Test
    public void testGet() throws IOException, InterruptedException {
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/test")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("OK")
                );

        HttpResponse<String> actual = restApiClient.get(URI.create("http://localhost:1081/test"));

        assertThat(actual.statusCode()).isEqualTo(200);
        assertThat(actual.body()).isEqualTo("OK");
    }

    @Test
    public void testDelete() throws IOException, InterruptedException {
        mockServerClient
                .when(
                        request()
                                .withMethod("DELETE")
                                .withPath("/test")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                );

        HttpResponse<String> actual = restApiClient.delete(URI.create("http://localhost:1081/test"));

        assertThat(actual.statusCode()).isEqualTo(200);
    }

    @Test
    public void testPost() throws IOException, InterruptedException {
        mockServerClient
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/test")
                                .withBody("{username: 'foo'}")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("OK")
                );

        HttpResponse<String> actual
                = restApiClient.post(
                        URI.create("http://localhost:1081/test"),
                        HttpRequest.BodyPublishers.ofString("{username: 'foo'}"));

        assertThat(actual.statusCode()).isEqualTo(200);
        assertThat(actual.body()).isEqualTo("OK");
    }

    @Test
    public void testPut() throws IOException, InterruptedException {
        mockServerClient
                .when(
                        request()
                                .withMethod("PUT")
                                .withPath("/test")
                                .withBody("{username: 'foo'}")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("OK")
                );

        HttpResponse<String> actual
                = restApiClient.put(
                URI.create("http://localhost:1081/test"),
                HttpRequest.BodyPublishers.ofString("{username: 'foo'}"));

        assertThat(actual.statusCode()).isEqualTo(200);
        assertThat(actual.body()).isEqualTo("OK");
    }

    @Test
    public void test404ErrorIsReturned() throws IOException, InterruptedException {
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/test")
                )
                .respond(
                        response()
                                .withStatusCode(404)
                );

        assertThatThrownBy(() -> restApiClient.get(URI.create("http://localhost:1081/test")))
                .isInstanceOfSatisfying(IOException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("the status code responded by external API is 404.");
                });
    }
}
