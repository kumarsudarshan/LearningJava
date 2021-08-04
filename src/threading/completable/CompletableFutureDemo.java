package threading.completable;

/*
A CompletableFuture is used for asynchronous programming.
Asynchronous programming means writing non-blocking code.
It runs a task on a separate thread than the main application thread and
notifies the main thread about its progress, completion or failure.

CompletionStage
It performs an action and returns a value when another completion stage completes.
A model for a task that may trigger other tasks.

Future vs. CompletableFuture
A CompletableFuture is an extension to Java's Future API which was introduced in Java 8.

A Future is used for asynchronous Programming. It provides two methods, isDone() and get().
The methods retrieve the result of the computation when it completes.

Limitations of the Future
A Future cannot be mutually complete.
We cannot perform further action on a Future's result without blocking.
Future has not any exception handling.
We cannot combine multiple futures.

 */

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // example of callable future and problems with blocking
        // in this case, it will behave like
//        callableFuture();

        // completable future
        completableFuture();

        // completable async future
//        completableFutureAsync();
    }

    public static void completableFuture() {
        Integer k = 0;
        for (int i = 0; i < 10; i++) {
            Integer finalK = ++k;
            CompletableFuture.supplyAsync(() -> new Order(finalK))
                    .thenApply(order -> order.fetchOrder())
                    .thenApply(order -> order.enrichOrder(order))
                    .thenApply(order -> order.payment(order))
                    .thenApply(order -> order.dispatch(order))
                    .thenAccept(order -> order.sendEmail(order));
        }
    }

    public static void completableFutureAsync() {
        CompletableFuture.supplyAsync(() -> new Order(1))
                .thenApplyAsync(order -> order.fetchOrder()) // this will run on separate thread
                .thenApplyAsync(order -> order.enrichOrder(order)) // this will also run separate thread
                .thenApply(order -> order.payment(order))
                .thenApply(order -> order.dispatch(order))
                .thenAccept(order -> order.sendEmail(order));

        ExecutorService cpuBound = Executors.newFixedThreadPool(10);
        ExecutorService ioBound = Executors.newFixedThreadPool(10);
        CompletableFuture.supplyAsync(() -> new Order(2), cpuBound)
                .thenApplyAsync(order -> order.fetchOrder()) // this will run on separate thread, since
                .thenApplyAsync(order -> order.enrichOrder(order), ioBound) // since it is Async, so we can also provide different thread pool
//                .thenApply(order -> order.payment(order), ioBound) // will get exception here bcoz we need to provide only cpuBound only
//                i.e. it should run on same thread pool
                .thenApply(order -> order.dispatch(order))
                .exceptionally(e -> failedOrder()) // if any exceptions come between the above methods, it will come here.
                .thenAccept(order -> order.sendEmail(order));
                // .thenCombine(); // we can combine with different CompletableFuture

        cpuBound.shutdown();
        ioBound.shutdown();
    }

    private static Order failedOrder() {
        // add failed order handle scenario
        return null;
    }

    public static void callableFuture() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);

        int k = 0;
        for (int i = 0; i < 10; i++) {
            Order order = new Order(++k);
            Future<Order> future1 = service.submit(order.fetchOrder());
            order = future1.get(); // blocking

            Future<Order> future2 = service.submit(order.enrichOrder(order));
            order = future2.get(); // blocking

            Future<Order> future3 = service.submit(order.payment(order));
            order = future3.get(); // blocking

            Future<Order> future4 = service.submit(order.dispatch(order));
            order = future4.get(); // blocking

            Future<Order> future5 = service.submit(order.sendEmail(order));
            order = future5.get(); // blocking
        }

        service.shutdown();
    }

}

class Order implements Callable {

    int id;
    String orderDetails;
    Map<String, Integer> items;
    boolean payment;
    String status;

    public Order(int id) {
        this.id = id;
    }


    @Override
    public Object call() throws Exception {
        return this;
    }

    public Order fetchOrder() {
        System.out.println("Fetched Order, ID: " + this.id);
        return this;
    }

    public Order enrichOrder(Order order) {
        if (this.id == order.id) {
            this.orderDetails = order.orderDetails;
            this.items = order.items;
        }
        System.out.println("added order details, Order ID: " + this.id);
        return this;
    }

    public Order payment(Order order) {
        // do payment steps
        // lets consider, payment will take 1 sec time to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.payment = true;
        System.out.println("payment success, Order ID: " + this.id);
        return this;
    }

    public Order dispatch(Order order) {
        System.out.println("dispatched, Order ID: " + this.id);
        // do dispatch process
        return order;
    }

    public Order sendEmail(Order order) {
        System.out.println("send mail, Order ID: " + this.id);
        // do send email process
        return order;
    }
}