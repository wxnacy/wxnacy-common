package com.wxnacy.common.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestBuilder {
    public static void main(String[] args) throws IOException {
        var client = RequestBuilder.getInstance();
        String filePath = "/Users/wxnacy/Downloads/test.jpg";
        String url = "http://localhost:8092/static/nvshens/gallery/19411/35323/s/001.jpg";
//        client.download(url, filePath);
        Map headers = new HashMap();
        headers.put("referer", "https://www.nvshens.org");
        url = "https://t1.onvshen.com:85/gallery/28131/35231/s/005.jpg";
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
//            System.out.println(contentType);
            File file  = new File(filePath);
            FileUtils.writeByteArrayToFile(file, response.body().bytes());
        }
    }
}
