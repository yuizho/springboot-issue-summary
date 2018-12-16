package io.github.yuizho.springbootissuesummary.infrastructure.rest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * The Http Client class of External Rest API.
 * This class depends on Java 11 Http Client (java.net.http.HttpClient).
 * https://openjdk.java.net/groups/net/httpclient/intro.html
 */
@Component
public class RestApiClient {
    public HttpResponse<String> get(URI uri) {
        return sendNobodyRequest(uri, "GET");
    }

    public HttpResponse<String> delete(URI uri) {
        return sendNobodyRequest(uri, "DELETE");
    }

    public HttpResponse<String> post(URI uri, HttpRequest.BodyPublisher bodyPublisher) {
        return sendRequest(uri, "POST", bodyPublisher);
    }

    public HttpResponse<String> put(URI uri, HttpRequest.BodyPublisher bodyPublisher) {
        return sendRequest(uri, "PUT", bodyPublisher);
    }

    protected HttpResponse<String> sendNobodyRequest(URI uri, String method) {
        HttpClient client = getHttpClient();
        HttpRequest request = getHttpRequest(uri, method, HttpRequest.BodyPublishers.noBody());
        return send(client, request);
    }

    protected HttpResponse<String> sendRequest(URI uri, String method, HttpRequest.BodyPublisher bodyPublisher) {
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

    protected HttpResponse<String> send(HttpClient client, HttpRequest request) {
        try {
            // TODO: request, responseをログ出力する
            HttpResponse<String> result
                    = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!isOK(result.statusCode())) {
                throw new IOException(
                        String.format("the status code responded by external API is %d.", result.statusCode()));
            }
            return result;
        } catch (IOException | InterruptedException e) {
            // TODO: まともにエラー処理する
            throw new RuntimeException(e);
        }
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
