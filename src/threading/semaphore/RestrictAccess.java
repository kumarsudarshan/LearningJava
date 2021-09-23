package threading.semaphore;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class RestrictAccess {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(3); // permits only 3 job at a time
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 50; i++) {
            service.execute(new Task(i, semaphore));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);

    }
}

class Task implements Runnable {
    int threadNumber;
    Semaphore semaphore;

    public Task(int threadNumber, Semaphore semaphore) {
        this.threadNumber = threadNumber;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        // do some job
        try {
            semaphore.acquire();
            System.out.println("Thread number: " + threadNumber);
            Thread.sleep(1000);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}