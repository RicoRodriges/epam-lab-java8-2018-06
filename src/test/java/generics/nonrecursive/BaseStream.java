package generics.nonrecursive;

public interface BaseStream<T> {

    BaseStream<T> parallel();
    BaseStream<T> sequential();
}
