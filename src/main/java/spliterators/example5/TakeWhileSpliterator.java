package spliterators.example5;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TakeWhileSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private Spliterator<T> spliterator;
    private Predicate<? super T> predicate;
    private boolean isEnd = false;

    public TakeWhileSpliterator(Spliterator<T> spliterator, Predicate<? super T> predicate) {
        super(spliterator.estimateSize(), spliterator.characteristics() & ~(SIZED | CONCURRENT));
        this.spliterator = spliterator;
        this.predicate = predicate;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (!isEnd) {
            Consumer<T> wrapper = (x) -> {
                isEnd = !predicate.test(x);
                if (!isEnd) {
                    action.accept(x);
                }
            };
            spliterator.tryAdvance(wrapper);
        }
        return !isEnd;
    }
}
