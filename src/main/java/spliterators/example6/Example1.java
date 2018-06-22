package spliterators.example6;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Example1 {

    public static void main(String[] args) {
        Stream<Long> stream = StreamSupport.stream(new SerialFibonacciSpliterator(), false);
        stream.forEachOrdered(System.out::println);
    }
}
