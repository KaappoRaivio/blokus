import java.util.List;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {

//        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/Fri Dec 14 14:26:31 CET 2018.ser", false);
        Board board = new Board();
        long aikaAlussa = System.currentTimeMillis();
        Board board1 = board.deepCopy();
        long aikaLopussa = System.currentTimeMillis();
        System.out.println((aikaLopussa - aikaAlussa) / 1000.0);
        System.exit(0);

        Piece piece19 = new Piece(Piece.PIECE_19, PieceType.BLUE);
        Piece piece18 = new Piece(Piece.PIECE_18, PieceType.BLUE).rotate(Orientation.DOWN, false);
        Piece piece5 = new Piece(Piece.PIECE_5, PieceType.YELLOW);
        Piece piece15 = new Piece(Piece.PIECE_15, PieceType.YELLOW);

        Piece piece1 = new Piece(Piece.PIECE_1, PieceType.RED);
        board.putOnBoard(0, 0, piece1);

        System.out.println(board);
        System.out.println(board1);
        System.out.println((aikaLopussa - aikaAlussa) / 1000.0);
        System.exit(0);



        List<Move> moves = board.getAllFittingMoves(PieceType.BLUE);


        for (Move move : moves) {
            System.out.println(move);
        }

        System.out.println(moves.size());


    }
}
