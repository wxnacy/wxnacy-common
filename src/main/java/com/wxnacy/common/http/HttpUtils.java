package com.wxnacy.common.http;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

public class HttpUtils {
    public static void main(String[] args) {
        try {
            String url = "http://wxnacy.com/images/mp.jpg";
            HttpResponse<InputStream> response = get(url);
            File file = new File("/Users/wxnacy/Downloads/index.jpg");
            FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(response.body()));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String toString(HttpResponse<InputStream> response) throws IOException {
        return IOUtils.toString(response.body(), StandardCharsets.UTF_8);
    }

    public static void download(String url, String filePath) throws IOException, InterruptedException {
        HttpResponse<InputStream> response = get(url);
        File file = new File(filePath);
        FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(response.body()));
    }

    public static void download(String url, String filePath, Map<String, String> headers) throws IOException, InterruptedException {
        HttpResponse<InputStream> response = get(url, null, headers, null);
        File file = new File(filePath);
        FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(response.body()));
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
