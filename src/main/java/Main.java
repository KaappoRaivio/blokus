import java.util.List;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board();
        System.out.println(board.splitBoardInto(NUMBER_OF_CORES));
    }
}
