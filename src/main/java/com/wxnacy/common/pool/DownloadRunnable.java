package com.wxnacy.common.pool;

import com.wxnacy.common.http.RequestBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DownloadRunnable implements Runnable {

    private RequestBuilder request;
    private String url;
    private String filePath;

    public DownloadRunnable(RequestBuilder request, String url, String filePath ) {
        this.request = request;
        this.url = url;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
        processCommand();
        System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
    }

    private void processCommand() {
        Map headers = new HashMap();
        headers.put("referer", "https://www.nvshens.org");

        try {
            request.download(this.url, filePath, headers);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public String toString() {
        return this.url;
    }
}