package futures;

import java.util.concurrent.*;

public class Example2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = ForkJoinPool.commonPool();

        Callable<Integer> integerCallable = () -> 42;
        Future<Integer> future = service.submit(integerCallable);
        System.out.println(future.get());
    }
}
