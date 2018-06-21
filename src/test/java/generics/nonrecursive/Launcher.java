package generics.nonrecursive;

public class Launcher {

    public static void main(String[] args) {
        BaseStream<String> baseStream = null;
        baseStream.sequential()
                  .parallel()
                  .sequential()
                  .parallel();

//        Stream<String> stream = null;
//        stream.filter(String::isEmpty)
//              .parallel()
//              .sequential()
//              .parallel()
//              .count();


        java.util.stream.Stream<String> stream = null;
        stream.sequential()
              .parallel()
              .filter(String::isEmpty)
              .parallel()
              .count();
    }
}
