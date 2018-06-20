package spliterators.example2;

import java.util.Spliterators;
import java.util.function.IntConsumer;

/**
 *                             [0, 1, 2, 3, 4, 5, 6, 7]
 * prefix (out): [0, 1, 2, 3]                           postfix (this): [4, 5, 6, 7]
 *
 */
public class IntArraySpliterator extends Spliterators.AbstractIntSpliterator {

    private static final long THRESHOLD = 5;
    private final int[] data;
    private int startInclusive;
    private final int endExclusive;

    public IntArraySpliterator(int[] data) {
        this(data, 0, data.length);
    }

    private IntArraySpliterator(int[] data, int startInclusive, int endExclusive) {
        super(data.length, SIZED | ORDERED | NONNULL | IMMUTABLE);
        this.data = data;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive == endExclusive) {
            return false;
        }
        action.accept(data[startInclusive++]);
        return true;
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
    public OfInt trySplit() {
        if (estimateSize() < THRESHOLD) {
            return null;
        }
        int mid = startInclusive + (int)(estimateSize() / 2);
        return new IntArraySpliterator(data, startInclusive, startInclusive = mid);
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while (startInclusive != endExclusive) {
            action.accept(data[startInclusive++]);
        }
    }
}
