package spliterators.example2;

import java.util.Spliterators;
import java.util.function.IntConsumer;

public class IntArraySpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[] data;
    private int index = 0;

    public IntArraySpliterator(int[] data) {
        this(data, data.length, SIZED | ORDERED | NONNULL | IMMUTABLE);
    }

    private IntArraySpliterator(int[] data, long est, int additionalCharacteristics) {
        super(est, additionalCharacteristics);
        this.data = data;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (index == data.length) {
            return false;
        }
        action.accept(data[index++]);
        return true;
    }

    @Override
    public OfInt trySplit() {
        return super.trySplit();
    }
}
