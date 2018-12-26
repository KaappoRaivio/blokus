public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(4));

        board.putOnBoard(0, 0, PieceID.PIECE_13, 3, Orientation.UP, true);
        board.putOnBoard(3, 3, PieceID.PIECE_14, 3, Orientation.DOWN, false);
        System.out.println(board);


    }

}
