package spliterators.example5;

import java.security.InvalidParameterException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import spliterators.example4.Pair;

public class ZipSpliterator<T, U> extends Spliterators.AbstractSpliterator<Pair<T, U>> {

    private Spliterator<T> spliteratorLeft;
    private Spliterator<U> spliteratorRight;

    public ZipSpliterator(Spliterator<T> spliteratorLeft, Spliterator<U> spliteratorRight) {
        super(spliteratorLeft.estimateSize(), spliteratorLeft.characteristics() & ~(CONCURRENT));
        if (spliteratorLeft.getExactSizeIfKnown() != spliteratorRight.getExactSizeIfKnown()) {
            throw new InvalidParameterException();
        }
        this.spliteratorLeft = spliteratorLeft;
        this.spliteratorRight = spliteratorRight;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Pair<T, U>> action) {
        return spliteratorLeft.tryAdvance(
            (x) -> spliteratorRight.tryAdvance(
                (y) -> action.accept(new Pair<>(x, y))));
    }
}
