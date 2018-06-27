package futures;

import java.util.concurrent.*;

public class Example2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = ForkJoinPool.commonPool();

        Callable<Integer> integerCallable = () -> {
            System.out.println("Started");
            TimeUnit.SECONDS.sleep(5);
            System.out.println("After sleeping");
            return 42;
        };
        Future<Integer> future = service.submit(integerCallable);
//        System.out.println(future.get());

        TimeUnit.SECONDS.sleep(1);
        System.out.println("Main end");

        service.shutdownNow();
        service.awaitTermination(10, TimeUnit.SECONDS);
//        System.out.println(future.isDone());
    }
}
