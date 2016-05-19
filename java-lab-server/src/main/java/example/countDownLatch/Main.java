package example.countDownLatch;


import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(4);

        new Worker(latch,"Sand").start();
        new Worker(latch,"Cement").start();
        new Worker(latch,"Water").start();
        new Worker(latch,"Breakstone").start();

        System.out.println("Waiting for all workers");
        latch.await();
        System.out.println("All workers finished. Now we can shake.");

    }
}
