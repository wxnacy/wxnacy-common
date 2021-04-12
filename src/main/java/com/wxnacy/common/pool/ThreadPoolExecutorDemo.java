package com.wxnacy.common.pool;

import com.wxnacy.common.http.RequestBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ThreadPoolExecutorDemo {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 9;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME =0L;

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        //使用阿里巴巴推荐的创建线程池的方式
        //通过ThreadPoolExecutor构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                CORE_POOL_SIZE, MAX_POOL_SIZE,
//                KEEP_ALIVE_TIME, TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>());

        List<Future<String>> futureList = new ArrayList<>();
        var client = RequestBuilder.getInstance();
        String filePath = "/Users/wxnacy/Downloads/test.jpg";
        String url = "https://t1.onvshen.com:85/gallery/28131/35231/s/005.jpg";
        for (int i = 0; i < 4; i++) {
            //创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Runnable worker = new DownloadRunnable(client, url, filePath);
            //执行Runnable
            executor.submit(worker);
        }
        for (int i = 0; i < 4; i++) {
            //创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Runnable worker = new DownloadRunnable(client, url, filePath);
            //执行Runnable
            executor.submit(worker);
        }
//        for (Future<String> fut : futureList) {
//            System.out.println("fut = " + fut);
//        }
        //终止线程池
//        executor.shutdown();
        while (!executor.isTerminated()) {
//            System.out.println("ThreadPoolExecutorDemo.main");
        }
        System.out.println("Finished all threads");
    }
}
