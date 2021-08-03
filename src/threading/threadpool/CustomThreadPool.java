package threading.threadpool;

import java.util.concurrent.*;
/*
Pool                            Queue Type                      Why?
FixedThreadPool                 LinkedBlockingQueue             Threads are limited, thus unbounded queue to store all tasks.
                                                                Note: Since queue can never become full, new threads are never created.
SingleThreadExecutor            LinkedBlockingQueue             ^^

CachedThreadPool                SynchronousQueue                Threads are unbounded, thus no need to store the tasks.
                                                                Synchronous queue is a queue with single slot

ScheduledThreadPool             DelayedWorkQueue                Special queue that deals with schedules/time-delays

Custom                          ArrayBlockingQueue              Bounded queue to store the tasks. If queue gets full,
                                                                new thread is created (as long as count is less than maxPoolSize)
 */
public class CustomThreadPool {
    public static void main(String[] args) {
        // custom pool
        customPool();

        // scenario to handle rejected tasks
        customRejectionHandle();
    }

    public static void customPool() {
        ExecutorService service = new ThreadPoolExecutor(
                10, // corePoolSize
                100, // maximumPoolSize
                120, // keepAliveTime
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(300) // capacity
        );

//        ExecutorService service = new ThreadPoolExecutor(
//                1, // corePoolSize
//                2, // maximumPoolSize
//                120, // keepAliveTime
//                TimeUnit.SECONDS,
//                new ArrayBlockingQueue<>(1) // capacity
//        );
//        example above, since 1 capacity is defined, so it will reject some tasks
//        Output:
//        Task rejected Task threading.threadpool.RunnableTask@61bbe9ba rejected from
//        java.util.concurrent.ThreadPoolExecutor@610455d6
//        [Running, pool size = 1, active threads = 1, queued tasks = 1, completed tasks = 0]
//        Thread name: pool-1-thread-1
//        Thread name: pool-1-thread-1
        try {
            for (int i = 0; i < 100; i++) {
                service.execute(new RunnableTask());
            }
        } catch (RejectedExecutionException e) {
            System.out.println("Task rejected " + e.getMessage());
        }
        service.shutdown();
    }

    public static void customRejectionHandle(){
        ExecutorService service = new ThreadPoolExecutor(
                1, // corePoolSize
                1, // maximumPoolSize
                120, // keepAliveTime
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), // capacity
                new CustomRejectionHandler()
        );

        try {
            for (int i = 0; i < 5; i++) {
                service.execute(new RunnableTask());
            }
        } catch (RejectedExecutionException e) {
            System.out.println("Task rejected " + e.getMessage());
        }
        service.shutdown();
        /*  Output:
            handling task rejection...
            handling task rejection...
            handling task rejection...
            Thread name: pool-1-thread-1
            Thread name: pool-1-thread-1
         */
    }


}

class CustomRejectionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // logging, operation to perform after task rejection
        System.out.println("handling task rejection...");
    }
}
class RunnableTask implements Runnable {
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