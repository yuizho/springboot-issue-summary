package io.github.yuizho.springbootissuesummary.infrastructure.rest;

import io.github.yuizho.springbootissuesummary.domain.adopters.LogCollector;
import io.github.yuizho.springbootissuesummary.domain.models.LogData;
import io.github.yuizho.springbootissuesummary.infrastructure.LogCollectorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * The Http Client class of External Rest API.
 * This class depends on Java 11 Http Client (java.net.http.HttpClient).
 * https://openjdk.java.net/groups/net/httpclient/intro.html
 */
@Component
public class RestApiClient {
    private final Logger logger = LoggerFactory.getLogger(RestApiClient.class);

    @Autowired
    private Map<String, LogCollector> logCollectors;

    @Autowired
    private LogCollectorProperties logCollectorProperties;

    public HttpResponse<String> get(URI uri)
            throws IOException, InterruptedException {
        return sendNobodyRequest(uri, "GET");
    }

    public HttpResponse<String> delete(URI uri)
            throws IOException, InterruptedException {
        return sendNobodyRequest(uri, "DELETE");
    }

    public HttpResponse<String> post(URI uri, HttpRequest.BodyPublisher bodyPublisher)
            throws IOException, InterruptedException {
        return sendRequest(uri, "POST", bodyPublisher);
    }

    public HttpResponse<String> put(URI uri, HttpRequest.BodyPublisher bodyPublisher)
            throws IOException, InterruptedException {
        return sendRequest(uri, "PUT", bodyPublisher);
    }

    protected HttpResponse<String> sendNobodyRequest(URI uri, String method)
            throws IOException, InterruptedException {
        HttpClient client = getHttpClient();
        HttpRequest request = getHttpRequest(uri, method, HttpRequest.BodyPublishers.noBody());
        return send(client, request);
    }

    protected HttpResponse<String> sendRequest(URI uri, String method, HttpRequest.BodyPublisher bodyPublisher)
            throws IOException, InterruptedException {
        HttpClient client = getHttpClient();
        HttpRequest request = getHttpRequest(uri, method, bodyPublisher);
        return send(client, request);
    }

    /**
     * Create HttpClient object of Java 11 Http Client.
     * If you want to change Http protocol version or proxy configuration,
     * please extends this class and override this method.
     *
     * @return HttpClient object
     */
    protected HttpClient getHttpClient() {
        return HttpClient.newBuilder()
                // this configuration is for redirect
                // when status code is 3xx and
                // the location url of response header is http and current url is https,
                // this redirect is not conducted. for more details, please refer this java doc.
                // https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.Redirect.html#NORMAL
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    protected HttpRequest getHttpRequest(URI uri, String method, HttpRequest.BodyPublisher bodyPublisher) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .method(method, bodyPublisher)
                .timeout(Duration.ofSeconds(10))
                .build();
    }

    protected HttpResponse<String> send(HttpClient client, HttpRequest request)
            throws IOException, InterruptedException {
        logger.info(request.toString());
        LogCollector logCollector = logCollectors.get(logCollectorProperties.getName());
        logCollector.collect(new LogData(getRequestLogStr(request)));
        HttpResponse<String> result
                = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (!isOK(result.statusCode())) {
            throw new IOException(
                    String.format("the status code responded by external API is %d.", result.statusCode()));
        }
        logger.info(result.toString());
        logCollector.collect(new LogData(getResponseLogStr(result)));
        return result;
    }

    String getRequestLogStr(HttpRequest request) {
        return String.format("HtttpRequest(Url: %s, Headers: %s, Body: %s)",
                request.uri().toString(),
                request.headers().map(),
                // TODO: bodyの内容が表示出来ない……
                request.bodyPublisher().orElse(HttpRequest.BodyPublishers.noBody()));
    }

    String getResponseLogStr(HttpResponse<String> result) {
        return String.format("HtttpResponse(Url: %s, Headers: %s, Body: %s)",
                result.uri().toString(),
                result.headers().map(),
                result.body());
    }

    /**
     * Check if the status is OK.
     *
     * @param status Http response status
     * @return is Ok or not
     */
    protected boolean isOK(int status) {
        return status == HttpStatus.OK.value();
    }
}
