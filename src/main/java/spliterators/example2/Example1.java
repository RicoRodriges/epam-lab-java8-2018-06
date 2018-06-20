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
//        int[] data = IntStream.rangeClosed(0, 100)
//                              .filter(value -> (value & 1) == 0)
//                              .toArray();

        int[] data = {0, 1, 2, 3, 4, 5, 6, 7};

        IntArraySpliterator spliterator = new IntArraySpliterator(data);

        StreamSupport.intStream(spliterator, true)
                     .forEach(System.out::println);
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
