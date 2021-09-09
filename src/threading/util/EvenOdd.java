package threading.util;

/*
Print even odd using 2 threads
 */
public class EvenOdd {

    public static void main(String[] args) {
        EvenOdd myThread = new EvenOdd();
        Runnable r1 = () -> myThread.printEven();
        Runnable r2 = () -> myThread.printOdd();

        new Thread(r1).start();
        new Thread(r2).start();
    }

    static int number = 1;
    static int N = 100;

    public void printEven() {
        synchronized (this) {
            while (number < N) {
                while (number % 2 != 0) {
                    try {
                        wait();
                    } catch (InterruptedException e) {

                    }
                }
                System.out.println(number);
                number++;
                notify();
            }
        }
    }

    public void printOdd() {
        synchronized (this) {
            while (number < N) {
                while (number % 2 == 0) {
                    try {
                        wait();
                    } catch (InterruptedException e) {

                    }
                }
                System.out.println(number);
                number++;
                notify();
            }
        }
    }
}
