package threading.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimeoutThread {
    public static void main(String[] args) {
        // will using thread pool instance work ?
        usingThreadPoolInstance(); // no

        // will using Callable Future work ?
        usingCallableFuture(); // no

        // java Thread cannot be killed, they are cooperative.
        // we need ask politely
        // But how to ask politely ?
        // 1. Interrupts 2. Volatile / AtomicBoolean

        // using thread interrupt, threadPool.shutdownNow(), future.cancel(true); // all process here called Thread.interrupt
        threadInterrupt();
        usingThreadPool();
        usingFuture();

        // Using Volatile and Atomic Boolean
        usingVolatile();
        usingAtomicBoolean();

        // But how to wait for 10 minutes

    }

    public static void stopUsingScheduler() {
        // 1. create a task and submit to a thread
        MyTask1 task = new MyTask1();
        Thread t1 = new Thread(task);
        t1.start();

        // 2. schedule stop after 10 minutes
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        scheduler.schedule(() -> {
            task.stop();
        }, 10, TimeUnit.MINUTES);
    }

    public static void stopUsingFuture() {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        // 1. create a task and submit to a thread
        MyTask1 task = new MyTask1();
        final Future<?> future = threadPool.submit(task);

        // 2. Wait for 10 minutes to get response

        try {
            future.get(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            future.cancel(true); // if using interrupts
            task.stop(); // if using volatile or AtomicBoolean
        }
    }

    public static void usingThreadPoolInstance() {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        // 1. start the task
        threadPool.submit(() -> {
            System.out.println("My Runnable task");
        });

        // 2. Time out for 10 minutes

        // 3. Stop the thread (by shutting down the pool)
        threadPool.shutdown();
        threadPool.shutdownNow();

        // problem with this is :
        // threadPool.shutdown() will not accept any new task, but previously submitted task will get executed.

        // threadPool.shutdownNow(); will not accept any new task, but previously tasks waiting in the queue are returned.
        // tasks being run by the threads are attempted to stop, but there is no guarantee.

        // so using thread pool will not help us
    }

    public static void usingCallableFuture() {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        // 1. start the task
        Future<?> future = threadPool.submit(() -> {
            // perform task

        });

        // 2. time out for 10 minutes

        // 3. stop the thread by cancelling the future
        future.cancel(true); // true means mayInterruptIfRunning, but it only attempt to stop the thread not gives any guarantee
    }

    public static void threadInterrupt() {
        // better to check in infinite loops or between steps

        // 1. Start the task
        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) { // keeps checking for interrupts
                // next step
            }
        });

        // 2. wait for 10 minutes

        // 3. stop the thread
        t.interrupt();
    }

    public static void usingThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        // 1. start the task
        threadPool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                // next step
            }
        });

        // 2. time out for 10 minutes

        // 3. stop the thread
        threadPool.shutdownNow(); // This internally calls Thread.interrupt for all running threads
    }

    public static void usingFuture() {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        // 1. start the task
        Future<?> future = threadPool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                // next step
            }
        });

        // 2. time out for 10 minutes

        // 3. stop the thread
        future.cancel(true); // true means mayInterruptIfRunning, This internally calls Thread.interrupt for thread running the task
    }

    public static void usingVolatile() {
        // 1. start the task
        MyTask task = new MyTask();
        Thread t1 = new Thread(task); // same will work for thread pool
        t1.start();

        // 2. timeout for 10 minutes

        // 3. ask task to stop using volatile.
        task.keepRunning = false;
    }

    public static void usingAtomicBoolean() {
        // 1. start the task
        MyTask1 task = new MyTask1();
        Thread t1 = new Thread(task); // same will work for thread pool
        t1.start();

        // 2. timeout for 10 minutes

        // 3. ask task to stop using atomic variable.
        task.stop();
    }
}

class MyTask implements Runnable {

    public volatile boolean keepRunning = true;

    @Override
    public void run() {
        while (keepRunning) {
            // next step
        }
    }
}

class MyTask1 implements Runnable {

    public AtomicBoolean keepRunning = new AtomicBoolean(true);

    @Override
    public void run() {
        while (keepRunning.get()) {
            // next step
        }
    }

    public void stop() {
        keepRunning.set(false);
    }
}