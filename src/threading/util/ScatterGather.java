package threading.util;

import java.util.*;
import java.util.concurrent.*;

/*
Write code to make IO calls (HTTP) to N sources, aggregate the response and return with a timeout of 3 seconds.
 */
public class ScatterGather {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        implementScatterGather();
    }

    public static void implementScatterGather() throws InterruptedException, ExecutionException, TimeoutException {
        Map<String, Integer> urlMap = new HashMap<>();
        urlMap.put("amazon", 1);
        urlMap.put("flipkart", 2);
        urlMap.put("walmart", 3);

        // Approach 1: Using Executor service
        System.out.println(getPricesUsingExecutorService(urlMap));

        // Approach 2: Using Completable Future
        System.out.println(getPricesUsingCompletableFuture(urlMap));
    }

    private static Map<String, Integer> getPricesUsingExecutorService(Map<String, Integer> urlMap) throws InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        Map<String, Integer> prices = Collections.synchronizedMap(new HashMap<>());

        CountDownLatch latch = new CountDownLatch(3);
        for (Map.Entry<String, Integer> item : urlMap.entrySet()) {
            threadPool.submit(new Task(item.getKey(), item.getValue(), prices, latch));
        }

        latch.await(3, TimeUnit.SECONDS); // wait for countDown to zero or timeout, whichever occur first.
        threadPool.shutdown();
        return prices;
    }

    private static Map<String, Integer> getPricesUsingCompletableFuture(Map<String, Integer> urlMap) throws InterruptedException, ExecutionException, TimeoutException {
        Map<String, Integer> prices = Collections.synchronizedMap(new HashMap<>());

        CountDownLatch latch = new CountDownLatch(3);
        CompletableFuture<Void>[] tasks = new CompletableFuture[urlMap.size()];
        int i = 0;
        for (Map.Entry<String, Integer> item : urlMap.entrySet()) {
            CompletableFuture<Void> task = CompletableFuture.runAsync(new Task(item.getKey(), item.getValue(), prices, latch));
            tasks[i++] = task;
        }

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(tasks);
        allTasks.get(3, TimeUnit.SECONDS); // wait for all tasks to complete but for 3 seconds only
        return prices;
    }
}

class Task implements Runnable {

    private Map<String, Integer> prices;
    private CountDownLatch latch;
    String url;
    int productId;

    public Task(String url, int productId, Map<String, Integer> prices, CountDownLatch latch) {
        this.url = url;
        this.productId = productId;
        this.prices = prices;
        this.latch = latch;
    }

    @Override
    public void run() {
        // make http call to get price, for eg generating random
        int price = new Random().nextInt(1000);

        prices.put(this.url, price);
        latch.countDown(); // add price and countdown, though if added after timeout, then ignored.
    }
}