package spliterators.example3;

import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.IntConsumer;

/**
 * 1 2 3 4 4
 * ---------
 * 5 6 7 1 3
 * 2 3 4 5 1
 *
 * 1 2 3 4 4
 * 5 6 / 7 1 3
 * 2 3 4 5 1
 */
public class UnfairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] data;
    private int startInclusive;
    private final int endExclusive;
    private int indexCurrentElement = 0;
    private int rowLength;


    public UnfairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length);
    }

    private UnfairRectangleSpliterator(int[][] data, int startInclusive, int endExclusive) {
        super(checkAndCalcSizeOfMatrix(data), SIZED | NONNULL | IMMUTABLE | ORDERED);
        this.data = data;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
        this.rowLength = data[0].length;
    }

    private static int checkAndCalcSizeOfMatrix(int[][] data) {
        Objects.requireNonNull(data);
        if (data.length == 0) {
            throw new IllegalArgumentException();
        }
        int rowLength = data[0].length;
        if (!Arrays.stream(data).allMatch(row -> row.length == rowLength)) {
            throw new IllegalArgumentException();
        }
        return data.length * rowLength;
    }

    @Override
    public long estimateSize() {
        return getExactSizeIfKnown();
    }

    @Override
    public long getExactSizeIfKnown() {
        return (endExclusive - startInclusive - 1) * rowLength + rowLength - indexCurrentElement;
    }

    @Override
    public OfInt trySplit() {
        // TODO
        return super.trySplit();
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive == endExclusive) {
            return false;
        }

        System.out.println(Thread.currentThread());

        action.accept(data[startInclusive][indexCurrentElement++]);
        if (indexCurrentElement == rowLength) {
            indexCurrentElement = 0;
            ++startInclusive;
        }
        return true;
    }


}
