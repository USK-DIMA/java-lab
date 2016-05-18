import java.util.concurrent.locks.Lock;

/**
 * Created by Dmitry on 17.05.2016.
 */
public class Main {

    public static void main(String[] args) {
       final TestLook testLook = new TestLook();
        new Thread() {
            public void run() {
                testLook.func("p1");
            }
        }.start();

        Thread thread2 = new Thread() {
            public void run() {
                testLook.func("p2");
            }
        };

        thread2.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();

    }
}
