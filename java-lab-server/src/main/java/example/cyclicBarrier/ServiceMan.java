package example.cyclicBarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ServiceMan {

    private CyclicBarrier queue;
    private List<String> inQueue;

    public ServiceMan(int hardWorking) {
        inQueue = new ArrayList<String>();

        /**Метод run запуститься, когда метод CyclicBarrier.await() вызовут hardWorking раз*/
        queue = new CyclicBarrier(hardWorking, new Runnable() {
            public void run() {
                System.out.println("Filling " + inQueue);
                inQueue.clear();/**Обнуление очереди*/
            }
        });
    }

    public void recharge(String name) {
        try {
            inQueue.add(name);
            queue.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

}