package threading.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
How to choose pool size?
- if we have CPU intensive type of work. i.e. how many app running on cpu.
then ideal pool size is cpu core count
- if we have IO intensive type of work. i.e. rest call or db call
then the ideal pool size is high. e.g. 100, 1000
 */

public class CPUCore {
    public static void main(String[] args) {
        // get count of available cores
        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println("CPU core count: " + coreCount);
        ExecutorService service = Executors.newFixedThreadPool(coreCount);

        // submit the tasks for execution
        for (int i = 0; i < 100; i++) {
            service.execute(new CPUIntensiveTask());
        }
        service.shutdown();
    }
}

class CPUIntensiveTask implements Runnable {

    @Override
    public void run() {
        System.out.println("run() method invoked...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}