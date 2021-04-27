package com.wxnacy.common.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

public class HttpUtils {
    public static void main(String[] args) {
        try {
            HttpResponse<InputStream> response = get("https://wxnacy.com/");
            System.out.println("toString(response) = " + toString(response));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String toString(HttpResponse<InputStream> response) throws IOException {
        return IOUtils.toString(response.body(), StandardCharsets.UTF_8);
    }

    public static HttpResponse<InputStream> get(String url) throws IOException, InterruptedException {
        return get(url, null, null, null);
    }

    public static HttpResponse<InputStream> get(String url, Map<String, String> params, Map<String, String> headers, Long timeout) throws IOException, InterruptedException {
        return request(HttpMethod.GET, url, params, headers, timeout);
    }

    private static HttpResponse<InputStream> request(HttpMethod method, String url, Map<String, String> params, Map<String, String> headers, Long timeout) throws IOException, InterruptedException {
        HttpClient.Builder clientBuilder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url));
        if (timeout != null) {
            clientBuilder = clientBuilder.connectTimeout(Duration.ofSeconds(timeout));
            builder = builder.timeout(Duration.ofSeconds(timeout));
        }
        if (method == HttpMethod.GET) {
            builder = builder.GET();
        } else if (method == HttpMethod.POST) {
            builder = builder.POST(null);
        }

        if ( headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.setHeader(key, value);
            }
        }

        HttpClient client = clientBuilder.build();
        HttpRequest request = builder.build();

        HttpResponse.BodyHandler<InputStream> inputStreamBodyHandler = HttpResponse.BodyHandlers.ofInputStream();
        return client.send(request, inputStreamBodyHandler);
    }
}
