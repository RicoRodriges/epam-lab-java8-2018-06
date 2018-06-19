package spliterators.example2;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Example1 {

    public static void main(String[] args) {
        int[] data = {1, 2, 3, 4, 5, 6};

        Arrays.stream(data)
              .forEachOrdered(System.out::println);

        IntArraySpliterator spliterator = new IntArraySpliterator(data);
        StreamSupport.intStream(spliterator, false)
                     .forEachOrdered(System.out::println);
        // TODO create a Stream
    }

    private static void createSpliterator() {
        int[] values = {0, 1, 2, 3, /* | */ 4, 5, 6, 7};

        IntStream stream = Arrays.stream(values);
        Spliterator.OfInt spliterator = stream.spliterator();

        Stream<Integer> stream1 = Stream.of(1, 2, 3, 4);
        Spliterator<Integer> spliterator1 = stream1.spliterator();

        List<Integer> integers = Arrays.asList(1, 2, 3, 4);
        Spliterator<Integer> spliterator2 = integers.spliterator();


        stream.forEachOrdered(seconds -> {
                  try {
                      TimeUnit.SECONDS.sleep(seconds);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              });
    }
}
