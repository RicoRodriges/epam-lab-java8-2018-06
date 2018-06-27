package futures;

import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Example3 {

    @Test
    public void constructorFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        assertFalse(future.isDone());

        future.complete(42);

        Integer result = future.get();
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
        assertEquals(42, result.intValue());
    }

    @Test
    public void completedFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.completedFuture(42);

        Integer result = future.get();
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
        assertEquals(42, result.intValue());
    }

    @Test
    public void supplyAsyncFuture() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 42);

        Integer result = future.join();

        assertTrue(future.isDone());
        assertEquals(42, result.intValue());
    }

    @Test
    public void thenAccept() throws InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                return 42;
            } catch (InterruptedException e) {
                return 60;
            }
        });

        CompletableFuture<Void> acceptFuture = future.thenAccept(System.out::println);


//        acceptFuture.join();

        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        commonPool.shutdown();
        while (!commonPool.awaitTermination(5, TimeUnit.SECONDS)) ;
        assertTrue(acceptFuture.isDone());
    }

    @Test
    public void thenRun() {
        Executor executor = Executors.newFixedThreadPool(4);

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread());
            return 42;
        }, executor)
                         .thenRunAsync(() -> {
                             System.out.println(Thread.currentThread());
                             System.out.println("Future completed");
                         }, executor)
                         .join();
    }


    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);


        CompletableFuture<Integer> future = CompletableFuture.completedFuture("123")
                                                             .thenApplyAsync(Integer::parseInt)
                                                             .thenApplyAsync(value -> value + 1, executor);

        executor.shutdown();
        future.thenAccept(System.out::println);
    }

    @Test
    public void thenApply() {
        ExecutorService executor = Executors.newFixedThreadPool(4);


        CompletableFuture<Integer> future = CompletableFuture.completedFuture("123")
                                                             .thenApplyAsync(Integer::parseInt)
                                                             .thenApplyAsync(value -> value + 1, executor);

        executor.shutdown();

        future.thenAccept(System.out::println);
        future.thenApply(value -> value - 1)
              .thenAccept(System.out::println);

        Integer result = future.join();
        assertEquals(124, result.intValue());
    }


    @Test
    public void thenApplyAsync() {
        CompletableFuture<StringBuilder> future = CompletableFuture.supplyAsync(() -> new StringBuilder("abc"));

        CompletableFuture<StringBuilder> future1 = future.thenApplyAsync(builder -> builder.append("1"));
        CompletableFuture<StringBuilder> future2 = future.thenApplyAsync(builder -> builder.append("2"));

        future1.join();
        future2.join();

        StringBuilder result = future.join();
        System.out.println(result);
    }

    @Test
    public void thenCombine() {
        CompletableFuture<Integer> left = CompletableFuture.completedFuture(10);
        CompletableFuture<String> right = CompletableFuture.completedFuture("20");

        left.thenCombine(right, (intValue, stringValue) -> Integer.parseInt(stringValue) + intValue)
            .thenAccept(System.out::println);
    }


    @Test
    public void thenCompose() {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.completedFuture("10")
                                                                               .thenCompose(this::parseIntAsync);
    }

    public Integer parseInt(String string) {
        return Integer.parseInt(string);
    }

    public CompletableFuture<Integer> parseIntAsync(String string) {
        return CompletableFuture.supplyAsync((() -> Integer.parseInt(string)));
    }

    @Test
    public void exceptionally() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> 42)
                                                          .thenApply(val -> {
                                                              val = val + 1;
                                                              if (false) {
                                                                  throw new RuntimeException("Wow");
                                                              }
                                                              return val;
                                                          })
                                                          .exceptionally(ex -> {
                                                              ex.printStackTrace();
                                                              return 43;
                                                          })
                                                          .thenAccept(System.out::println);


        future.join();
        System.out.println(future.isDone());
    }

    @Test
    public void handle() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> 42)
                                                          .thenApply(val -> {
                                                              val = val + 1;
                                                              if (true) {
                                                                  throw new RuntimeException("Wow");
                                                              }
                                                              return val;
                                                          })
                                                          .handle((val, ex) -> {
                                                              if (val == null) {
                                                                  ex.printStackTrace();
                                                                  return "43";
                                                              } else {
                                                                  System.out.println("Successfully");
                                                                  return String.valueOf(val + 1);
                                                              }
                                                          });

        future.join();
        System.out.println(future.isDone());
    }

    @Test
    public void anyOf() {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 42;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 43;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Object> result = CompletableFuture.anyOf(future1, future2);

        System.out.println(((Integer) result.join()).intValue());
    }

    @Test
    public void name() {
        CompletableFuture<Integer> handle = CompletableFuture.completedFuture("123")
                                                             .handle((res, ex) -> {
                                                                 return 42;
                                                             });

    }
}
