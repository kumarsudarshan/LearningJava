package threading.threadgroup;

public class ThreadGroupDemo {
    public static void main(String[] args) {
        // Current ThreadGroup is main group
        System.out.println(Thread.currentThread().getThreadGroup().getName()); // main

        // Every ThreadGroup is the child group of system group either directly or indirectly
        System.out.println(Thread.currentThread().getThreadGroup().getParent().getName()); // system

        // creating under main ThreadGroup
        ThreadGroup pg = new ThreadGroup("Parent Group");
        System.out.println(pg.getParent().getName()); // main

        // Creating under parent group
        ThreadGroup cg = new ThreadGroup(pg, "Child Group");
        System.out.println(cg.getParent().getName()); // Parent Group

        ThreadGroup g1 = new ThreadGroup("tg");
        Thread t1 = new Thread(g1, "Thread 1");
        Thread t2 = new Thread(g1, "Thread 2");
        g1.setMaxPriority(3);
        Thread t3 = new Thread(g1, "Thread 3");
        System.out.println(t1.getPriority()); // 5
        System.out.println(t2.getPriority()); // 5
        System.out.println(t3.getPriority()); // 3
    }
}
