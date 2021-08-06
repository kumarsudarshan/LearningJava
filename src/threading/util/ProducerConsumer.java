package threading.util;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        // 1. Using blocking queue
        usingBlockingQueue();

        // Without using blocking queue
        // We have 2 options to do that
        // 1. Locks and condition
        // 2. Wait-Notify

        // Let's create our own blocking queue and use lock condition
        // Refer implementation of MyBlockingQueue and use same logic in usingBlockingQueue() method

        // Let's create using wait-notify
        // Refer implementation of MyCustomBlockingQueue and use same logic in usingBlockingQueue() method

    }



    public static void usingBlockingQueue() throws InterruptedException {
        BlockingQueue<Item> queue = new ArrayBlockingQueue<>(10); // initial capacity

        // Producer
        final Runnable producer = () -> {
            while (true) {
                try {
                    Item item = createItem();
                    queue.put(item);
                    System.out.println("Item pushed : [" + item.eventType + ": " + item.message + "]");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // running 2 producer thread
        new Thread(producer).start();
        new Thread(producer).start();

        // Consumer
        final Runnable consumer = () -> {
            while (true) {
                Item item = null;
                try {
                    item = queue.take();
                    processItem(item);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        // running 2 consumer thread
        new Thread(consumer).start();
        new Thread(consumer).start();

    }

    public static Item createItem() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("CREATED", "user1 account created."));
        itemList.add(new Item("CREATED", "user2 account created."));
        itemList.add(new Item("CREATED", "user3 account created."));
        itemList.add(new Item("CREATED", "user4 account created."));
        itemList.add(new Item("CREATED", "user5 account created."));
        itemList.add(new Item("CREATED", "user6 account created."));
        itemList.add(new Item("CREATED", "user7 account created."));
        return itemList.get(new Random().nextInt(6));
    }

    public static void processItem(Item item) {
        System.out.println("Item comsumed: [" + item.eventType + ": " + item.message + "]");
    }
}

class Item {
    String eventType;
    String message;

    public Item(String eventType, String message) {
        this.eventType = eventType;
        this.message = message;
    }
}

class MyBlockingQueue<T> {
    private Queue<T> queue;
    private int capacity = 16;
    private ReentrantLock lock = new ReentrantLock(true); // fair
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public MyBlockingQueue(int capacity) {
        queue = new LinkedList<>();
        this.capacity = capacity;
    }

    public void put(T t) {
        lock.lock();
        try {
            if (queue.size() == capacity) { // block un-till queue has 1 slot empty to add item.
                // block the thread
                notFull.await(); // wait for not full
            }
            queue.add(t); // protected by lock
            notEmpty.signalAll(); // signal for not empty
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T take() {
        lock.lock();
        try {
            if (queue.size() == 0) { // block at-least queue has 1 item
                // block the thread
                notEmpty.await(); // wait for jot empty
            }
            T item = queue.poll(); // protected by same lock
            notFull.signalAll(); // signal for not full
            return item;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
}

class MyCustomBlockingQueue<T> {
    private Queue<T> queue;
    private int capacity = 16;
    private ReentrantLock lock = new ReentrantLock(true); // fair
    private Object notEmpty = new Object();
    private Object notFull = new Object();

    public MyCustomBlockingQueue(int capacity) {
        queue = new LinkedList<>();
        this.capacity = capacity;
    }

    public synchronized void put(T t) {
        while (queue.size() == capacity) {
            try {
                notFull.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        queue.add(t);
        notEmpty.notifyAll();
    }

    public T take() {
        if (queue.size() == 0) {
            try {
                notEmpty.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        T item = queue.poll();
        notFull.notifyAll();
        return item;
    }
}