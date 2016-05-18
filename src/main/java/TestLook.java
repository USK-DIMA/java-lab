import java.util.concurrent.locks.ReentrantLock;

public class TestLook {

    ReentrantLock lock = new ReentrantLock();

    public void func(String value){
            System.out.println(value+" 1 "+lock.isLocked());
            System.out.println(value+" Length:"+lock.getQueueLength());
            System.out.println(value+" 2 "+lock.isLocked());
            try {

                for(int i=0; i<10; i++) {
                    Thread.sleep(100);
                    System.out.println(value);
                }
                System.out.println(value+" Length:"+lock.getQueueLength());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println(value+" 3 "+lock.isLocked());
            }
            System.out.println(value+" 4 "+lock.isLocked());

    }
}
