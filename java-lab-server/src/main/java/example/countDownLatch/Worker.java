package example.countDownLatch;

import java.util.concurrent.CountDownLatch;

public class Worker extends Thread {

    private CountDownLatch countDownLatch;
    private String name;

    public Worker (CountDownLatch cdl, String name) {
        countDownLatch = cdl;
        this.name = name;
    }

    @Override
    public void run() {


        System.out.println(name + " working...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
        countDownLatch.countDown();

    }
}