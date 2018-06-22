package spliterators.example6;

import java.util.Comparator;
import java.util.Spliterators;
import java.util.function.LongConsumer;

public class SerialFibonacciSpliterator extends Spliterators.AbstractLongSpliterator {

    private static final int BOUND_INDEX = 92;
    private long prev = 0;
    private long curr = 1;
    private int index = 0;

    public SerialFibonacciSpliterator() {
        super(BOUND_INDEX, ORDERED | SORTED | NONNULL | SIZED | IMMUTABLE);
    }

    @Override
    public boolean tryAdvance(LongConsumer action) {
        if (index == BOUND_INDEX) {
            return false;
        }
        if (index != 0) {
            long next = prev + curr;
            prev = curr;
            curr = next;
        }
        action.accept(curr);
        ++index;
        return true;
    }

    @Override
    public void forEachRemaining(LongConsumer action) {
        while (index != BOUND_INDEX) {
            if (index != 0) {
                long next = prev + curr;
                prev = curr;
                curr = next;
            }
            action.accept(curr);
            ++index;
        }
    }

    @Override
    public long estimateSize() {
        return getExactSizeIfKnown();
    }

    @Override
    public long getExactSizeIfKnown() {
        return BOUND_INDEX - index;
    }

    @Override
    public Comparator<? super Long> getComparator() {
        return null;
    }
}
