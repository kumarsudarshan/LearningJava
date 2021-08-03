package threading.threadpool;

import java.util.concurrent.*;

public class PoolTypes {
    public static void main(String[] args) throws InterruptedException {
        // Type 1: newFixedThreadPool
        newFixedThreadPool();

        // Type 2: newCachedThreadPool
        newCachedThreadPool();

        // Type 3: scheduled executor
        newScheduledThreadPool();

        // Type 4: single threaded pool
        newSingleThreadExecutor();
    }

    public static void newFixedThreadPool() {
        // create the pool
        ExecutorService service = Executors.newFixedThreadPool(10);
        ThreadPoolExecutor myThreadPool = (ThreadPoolExecutor) service;

        // submit the tasks for execution
        for (int i = 0; i < 100; i++) {
            service.execute(new Task());
        }
        System.out.println("Thread name: " + Thread.currentThread().getName());
        System.out.println("Total number threads scheduled: " + myThreadPool.getTaskCount());
        service.shutdown();
    }

    public static void newCachedThreadPool() {
        // create the pool
        ExecutorService service = Executors.newCachedThreadPool();
        ThreadPoolExecutor myThreadPool = (ThreadPoolExecutor) service;
        // submit the tasks for execution
        for (int i = 0; i < 10; i++) {
            service.execute(new Task());
        }
        System.out.println("Thread name: " + Thread.currentThread().getName());
        System.out.println("Total number threads scheduled: " + myThreadPool.getTaskCount());
        service.shutdown();
    }

    public static void newScheduledThreadPool() throws InterruptedException {
        // for scheduling of tasks
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

        // task to run after 10 seconds of delay
        service.schedule(new Task(), 10, TimeUnit.SECONDS);

        // task to run repeatedly every 10 seconds
        service.scheduleAtFixedRate(new Task(), 15, 10, TimeUnit.SECONDS);

        // task to run repeatedly 10 seconds after previous task completes
        service.scheduleWithFixedDelay(new Task(), 15, 10, TimeUnit.SECONDS);

        Thread.sleep(60 * 1000);
        service.shutdown();
    }

    public static void newSingleThreadExecutor() throws InterruptedException {
        // for scheduling of tasks
        ExecutorService service = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 10; i++) {
            service.submit(new Task());
        }
        System.out.println("Thread name: " + Thread.currentThread().getName());
        service.shutdown();
    }
}

class Task implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread name: " + Thread.currentThread().getName());
    }
}