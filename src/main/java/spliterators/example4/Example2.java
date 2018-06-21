package spliterators.example4;

import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class Example2 {

    public static void main(String[] args) {
        String[] data = {"a", "b", "c"};

        IntStream.range(0, data.length)
                 .filter(index -> (index & 1) == 0)
                 .mapToObj(index -> data[index])
                 .forEachOrdered(System.out::println);

//        AtomicInteger index = new AtomicInteger();
//        Arrays.stream(data)
//              .parallel()
//              .filter(element -> (index.getAndIncrement() & 1) == 0)
//              .forEachOrdered(System.out::println);

//        AtomicLong index2 = new AtomicLong();
//        Arrays.stream(data)
//              .map(value -> new IndexedValue<>(index2.getAndIncrement(), value))
//              .filter(pair -> (pair.getIndex() & 1) == 0)
//              .forEachOrdered(System.out::println);


        IndexedArraySpliterator<String> spliterator = new IndexedArraySpliterator<>(data);
        StreamSupport.stream(spliterator, false)
                     .filter(pair -> (pair.getIndex() & 1) == 0)
                     .map(IndexedValue::getValue2)
                     .forEachOrdered(System.out::println);
    }

}
