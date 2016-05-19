package example.cyclicBarrier;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Printer implements Runnable {

    private String name;
    private Random rand;
    private ServiceMan serviceMan;

    public Printer(ServiceMan serviceMan, String name) {
        this.name = name;
        this.serviceMan = serviceMan;
        this.rand = new Random();
    }

    public void run() {
        try {
                TimeUnit.SECONDS.sleep(rand.nextInt(10));
                System.out.println(name + " is empty");
                serviceMan.recharge(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}