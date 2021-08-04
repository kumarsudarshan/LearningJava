package threading.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class CallableFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // create the pool
        ExecutorService service = Executors.newFixedThreadPool(10);

        // submit the task for execution
        List<Future> allFutures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // future here is a placeholder, which will return result sometime in future.
            Future<Integer> future = service.submit(new MyCallableTask());
            allFutures.add(future);
        }
        // 10 futures with 10 placeholders
        // perform some unrelated operations

        // after 10 seconds
        for (Future<Integer> future : allFutures) {
            System.out.println(future.get()); // blocking

            // blocking for some time
            // future.get(1, TimeUnit.SECONDS); // throws TimeoutException

            // cancel the task
            System.out.println("Future cancel: " + future.cancel(false)); // mayInterruptIfRunning

            // Returns true, if task was cancelled
            System.out.println("Future is cancelled: " +future.isCancelled());

            // returns true if task is completed (successfully or otherwise)
            System.out.println("Is future is done: " + future.isDone());
        }

        service.shutdown();
    }
}

class MyCallableTask implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Thread.sleep(1000);
        return new Random().nextInt(100);
    }
}