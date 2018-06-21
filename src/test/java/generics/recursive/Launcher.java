package generics.recursive;

public class Launcher {

    public static void main(String[] args) {
        Stream<String> stream = null;
        stream.sequential()
              .parallel()
              .sequential()
              .filter(String::isEmpty)
              .parallel()
              .count();
    }
}
