package example.cyclicBarrier;

public class Main {
    public static void main(String args[]) {
        ServiceMan serviceMan = new ServiceMan(3);

        for (int i = 0; i < 15; i++) {
            new Thread(new Printer(serviceMan, "Printer " + (i + 1))).start();
        }
    }
}
