package io.github.yuizho.springbootissuesummary.infrastructure.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class AbstractApiClient {
    public HttpResponse<String> fetch() {
        HttpClient client = getHttpClient();
        HttpRequest request = getHttpRequest();
        try {
            // TODO: request, responseをログ出力する
            // TODO: statusチェックはgithubのドキュメントよく読んでやる
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // TODO: まともにエラー処理する
            throw new RuntimeException();
        }
    }

    protected HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }

    protected HttpRequest getHttpRequest() {
        return HttpRequest.newBuilder()
                .uri(getURI())
                .build();
    }

    abstract URI getURI();
}
