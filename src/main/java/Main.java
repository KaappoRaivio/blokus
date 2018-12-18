import java.util.List;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Span span = new Span(new Position(5, 0), new Position(6, 2));

        span.forEach(System.out::println);
    }
}
