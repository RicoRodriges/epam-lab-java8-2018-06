package futures;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class Example1 {

    public static void main(String[] args) throws InterruptedException {
        HolderResult<Integer> result = new HolderResult<>();

        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                result.putResult(42);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(task).start();
        System.out.println(result.getResult());
    }
}

class HolderResult<T> {

    private BlockingQueue<T> result = new SynchronousQueue<>();

    public void putResult(T result) throws InterruptedException {
        this.result.put(result);
    }

    public T getResult() throws InterruptedException {
        return result.take();
    }
}
