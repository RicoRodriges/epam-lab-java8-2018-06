package generics.nonrecursive;

import java.util.function.Predicate;

public interface Stream<T> extends BaseStream<T> {

    Stream<T> filter(Predicate<? super T> predicate);

    long count();
}
