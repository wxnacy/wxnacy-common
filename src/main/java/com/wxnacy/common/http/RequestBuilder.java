package com.wxnacy.common.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestBuilder {
    public static void main(String[] args) throws IOException {
        var client = RequestBuilder.getInstance();
        String filePath = "/Users/wxnacy/Downloads/test.jpg";
        String url = "http://wxnacy.com/images/mp.jpg";
//        client.download(url, filePath);
//        headers.put("referer", "https://www.nvshens.org");
        Map headers = new HashMap();
        URL uri = new URL(url);
        System.out.println(uri.getPath());
        client.download(url, filePath, headers);
    }


    private static RequestBuilder requestBuilder;
    private OkHttpClient client = new OkHttpClient();
    private RequestBuilder() {};
    public synchronized static RequestBuilder getInstance() {
        if ( requestBuilder == null ){
            requestBuilder = new RequestBuilder();
        }
        return requestBuilder;
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


    public void download(String url, String filePath) throws IOException {
        this.download(url, filePath, null);
    }

    public void download(String url, String filePath, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder() .url(url);
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
            iterator.forEachRemaining(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addHeader(key, value);
            });
        }
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            String contentType = response.header("Content-Type");
            File file  = new File(filePath);
            FileUtils.writeByteArrayToFile(file, response.body().bytes());
        }
    }
}
