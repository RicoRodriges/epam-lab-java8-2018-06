package generics.recursive;

import java.util.function.Predicate;

public interface Stream<T> extends BaseStream<T, Stream<T>> {

    Stream<T> filter(Predicate<? super T> predicate);

    long count();
}
