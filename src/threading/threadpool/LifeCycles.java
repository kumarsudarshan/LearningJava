package threading.threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LifeCycles {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(1);
        for(int i = 0 ; i < 10; i++){
            service.submit(new LifeCycleTask());
        }

        // initiate shutdown
        service.shutdown();

        // will throw RejectionExecutionException, if try to execute after shutdown
        // service.execute(new LifeCycleTask());

        // will return true since shutdown has begun
        System.out.println("Is Shutdown: " + service.isShutdown());

        // will return true if all tasks are completed, including queued ones
        System.out.println("Is Terminated: " + service.isTerminated());

        // block until all tasks are completed or if timeout occurs
        try {
            service.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // will initiate shutdown   and return all queued tasks
        List<Runnable> runnables = service.shutdownNow();
        System.out.println(runnables);
    }
}

class LifeCycleTask implements Runnable {
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