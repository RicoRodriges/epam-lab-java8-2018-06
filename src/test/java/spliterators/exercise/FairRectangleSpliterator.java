package spliterators.exercise;

import java.util.Spliterators;
import java.util.function.IntConsumer;

/**
 * Сплитератор, оборачивающий прямоугольную матрицу int[][]
 * Обходит элементы слева-направо, сверху-вниз
 * Деление "честное" - по количеству элементов
 */
public class FairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    /*
     *  0  1  2  3  4
     *  5  6  / 7  8  9
     * 10 11 12 13 14
     */

    private static final long THRESHOLD = 7;
    private int[][] data;
    private int startInclusive;
    private final int endExclusive;

    public FairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length * data[0].length);
    }

    private FairRectangleSpliterator(int[][] data, int startInclusive, int endExclusive) {
        super(endExclusive - startInclusive, IMMUTABLE | NONNULL | ORDERED | SIZED | SUBSIZED);
        this.data = data;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    @Override
    public OfInt trySplit() {
        if (estimateSize() < THRESHOLD) {
            return null;
        }
        int mid = startInclusive + (int) (estimateSize() / 2);
        return new FairRectangleSpliterator(data, startInclusive, startInclusive = mid);
    }

    @Override
    public long estimateSize() {
        return getExactSizeIfKnown();
    }

    @Override
    public long getExactSizeIfKnown() {
        return endExclusive - startInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive != endExclusive) {
            action.accept(data[startInclusive / data[0].length][startInclusive % data[0].length]);
            ++startInclusive;
            return true;
        }
        return false;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while (startInclusive != endExclusive) {
            action.accept(data[startInclusive / data[0].length][startInclusive % data[0].length]);
            ++startInclusive;
        }
    }
}