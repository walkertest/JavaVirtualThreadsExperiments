package com.scaledcode.vthreads.apicalls;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class ApiCalls {
    public static  int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();
    public static  int WORK_ITEMS = 100;
//            NUMBER_OF_PROCESSORS;
    private final ExecutorService executorService;

    private static long rpcCost = 1000;

    public static CountDownLatch countDownLatch;

    public ApiCalls(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public final static Consumer<String> worker = (identifier) -> {
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Worker " + identifier + " Current thread: " + Thread.currentThread());
            new URL("https://httpstat.us/200?sleep="+rpcCost).getContent();
            long end = System.currentTimeMillis();
            System.out.println("Worker " + identifier + " Finishing with: " + Thread.currentThread() + " cost:" + (end-startTime));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            countDownLatch.countDown();
        }
    };

    public void execute() {
        countDownLatch = new CountDownLatch(WORK_ITEMS);
        long startTime = System.currentTimeMillis();
        System.out.println("Processing " + WORK_ITEMS + " items with " + NUMBER_OF_PROCESSORS + " threads.");
        try(ExecutorService service = executorService) {
            for(int i=0; i < WORK_ITEMS; i++) {
                String taskIdentifier = Integer.toString(i);

                service.execute(() -> worker.accept(taskIdentifier));
            }
        }
        System.out.println("Processing took: " + (System.currentTimeMillis() - startTime) + " ms.");
    }

    public static int getWorkItems() {
        return WORK_ITEMS;
    }

    public static void setWorkItems(int workItems) {
        WORK_ITEMS = workItems;
    }

    public static CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public static void setCountDownLatch(CountDownLatch countDownLatch) {
        ApiCalls.countDownLatch = countDownLatch;
    }
}
