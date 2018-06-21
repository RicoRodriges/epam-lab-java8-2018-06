package spliterators.example3;

import java.util.stream.StreamSupport;

public class Example1 {

    public static void main(String[] args) {
        int[][] data = new int[500][500];
        for (int i = 0, current = 0; i < data.length; ++i) {
            for (int j = 0; j < data.length; ++j) {
                data[i][j] = current++;
            }
        }

        UnfairRectangleSpliterator spliterator = new UnfairRectangleSpliterator(data);

        StreamSupport.intStream(spliterator, true)
                     .forEachOrdered(System.out::println);
    }
}
