package threading.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        PrintJob[] jobs = {
                new PrintJob("Kumar"),
                new PrintJob("Sudarshan"),
                new PrintJob("Nagendra"),
                new PrintJob("Pavan"),
                new PrintJob("Bhaskar"),
                new PrintJob("Varma")
        };
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (PrintJob job : jobs) {
            service.submit(job);
        }
        service.shutdown();
    }
}

class PrintJob implements Runnable {
    String name;

    PrintJob(String name) {
        this.name = name;
    }

    public void run() {
        System.out.println(name + "....Job Started By Thread:" + Thread.currentThread().getName());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        System.out.println(name + "....Job Completed By Thread:" + Thread.currentThread().getName());
    }
}

/*
Kumar....Job Started By Thread:pool-1-thread-1
Nagendra....Job Started By Thread:pool-1-thread-3
Sudarshan....Job Started By Thread:pool-1-thread-2
Sudarshan....Job Completed By Thread:pool-1-thread-2
Kumar....Job Completed By Thread:pool-1-thread-1
Nagendra....Job Completed By Thread:pool-1-thread-3
Pavan....Job Started By Thread:pool-1-thread-2
Bhaskar....Job Started By Thread:pool-1-thread-1
Varma....Job Started By Thread:pool-1-thread-3
Bhaskar....Job Completed By Thread:pool-1-thread-1
Varma....Job Completed By Thread:pool-1-thread-3
Pavan....Job Completed By Thread:pool-1-thread-2
 */