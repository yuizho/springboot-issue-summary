package io.github.yuizho.springbootissuesummary.infrastructure.api;

import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * The abstract class of Http Client to fetch data.
 * This class depends on Java 11 Http Client (java.net.http.HttpClient).
 * https://openjdk.java.net/groups/net/httpclient/intro.html
 */
public abstract class AbstractApiClient {
    /**
     * Fetch data from external API
     *
     * @return HttpResponse object
     */
    public HttpResponse<String> fetch() {
        HttpClient client = getHttpClient();
        HttpRequest request = getHttpRequest();
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

    /**
     * Create HttpRequest object of Java 11 Http Client.
     * If you want to change Https request method or something,
     * please extends this class and override this method.
     *
     * @return HttpRequest object
     */
    protected HttpRequest getHttpRequest() {
        return HttpRequest.newBuilder()
                .uri(getURI())
                .timeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Check if the status is OK.
     *
     * @param status Http response status
     * @return is Ok or not
     */
    protected boolean isOK(int status) {
        if (status == HttpStatus.OK.value()) {
            return true;
        }
        return false;
    }

    /**
     * return URI to request.
     *
     * @return URI to request
     */
    abstract URI getURI();
}
