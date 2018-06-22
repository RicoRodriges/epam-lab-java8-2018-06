package spliterators.example5;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DropWhileSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private final Spliterator<T> source;
    private final Predicate<? super T> predicate;
    private boolean dropped;

    // 000000000001000
    // |
    // 000000001000000
    // 000000001001000
    // ~
    // 111111110110111
    // &
    // 000010010101010
    // 000010010100010
    public DropWhileSpliterator(Spliterator<T> source, Predicate<? super T> predicate) {
        super(source.estimateSize(), source.characteristics() & ~(SIZED | SUBSIZED));
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (dropped) {
            return source.tryAdvance(action);
        }
        do {} while (!dropped && source.tryAdvance(value -> {
            if (!predicate.test(value)) {
                action.accept(value);
                dropped = true;
            }
        }));
        return dropped;
    }

    @Override
    public Spliterator<T> trySplit() {
        throw new UnsupportedOperationException();
    }
}
