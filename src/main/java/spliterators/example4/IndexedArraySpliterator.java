package spliterators.example4;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class IndexedArraySpliterator<T> extends Spliterators.AbstractSpliterator<IndexedValue<T>> {

    private final T[] data;
    private int index = 0;

    public IndexedArraySpliterator(T[] data) {
        super(data.length, SIZED | IMMUTABLE | ORDERED);
        this.data = data;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedValue<T>> action) {
        if (index == data.length) {
            return false;
        }
        action.accept(new IndexedValue<>((long)index, data[index++]));
        return true;
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedValue<T>> action) {
        while (index != data.length) {
            action.accept(new IndexedValue<>((long)index, data[index++]));
        }
    }

    @Override
    public Spliterator<IndexedValue<T>> trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return getExactSizeIfKnown();
    }

    @Override
    public long getExactSizeIfKnown() {
        return data.length - index;
    }
}
