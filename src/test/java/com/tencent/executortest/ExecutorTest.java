package com.tencent.executortest;

import com.scaledcode.vthreads.apicalls.ApiCalls;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorTest {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ApiCalls.setCountDownLatch(countDownLatch);
        ExecutorService service = Executors.newFixedThreadPool(ApiCalls.NUMBER_OF_PROCESSORS);
        service.execute(() -> {ApiCalls.worker.accept("1");});
        ApiCalls.countDownLatch.await();
        System.out.println("Processing took: " + (System.currentTimeMillis() - startTime) + " ms.");


    }
}
