public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board<Piece> board = new Board<>(14, 14, 4, new MyPieceManager<Piece>(4));

        board.putOnBoard(0, 0, PieceID.PIECE_13, 3, Orientation.RIGHT, false);
        board.putOnBoard(3, 3, PieceID.PIECE_14, 0, Orientation.DOWN, false);
        System.out.println(board);


    }

}
