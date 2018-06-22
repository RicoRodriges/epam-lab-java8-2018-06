package spliterators.example5;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Example1 {

    public static void main(String[] args) {
        List<String> strings = new LinkedList<>(Arrays.asList("a", "b", "c1", "d2", "e3", "q", "w", "e"));

//        List<String> result = IntStream.range(0, strings.size())
//                                       .filter(index -> (index & 1) == 0)
//                                       .mapToObj(strings::get)
//                                       .collect(Collectors.toList());
//
//
//        List<String> res = new AdvancedStreamImpl<>(strings.stream()).zipWithIndex()
//                                                                     .filter(pair -> (pair.getIndex() & 1) == 0)
//                                                                     .map(IndexedValue::getValue2)
//                                                                     .collect(Collectors.toList());


        new AdvancedStreamImpl<>(strings.stream()).dropWhile(string -> string.length() < 2)
                                                  .forEachOrdered(System.out::println);

    }
}
