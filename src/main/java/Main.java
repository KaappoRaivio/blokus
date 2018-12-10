public class Main {
    public static void main (String[] args) {
        Board board = new Board();


        Piece piece19 = new Piece(Piece.PIECE_19, Piece.BLUE);
        Piece piece18 = new Piece(Piece.PIECE_18, Piece.BLUE).rotate(Orientation.DOWN, false);
        Piece piece5 = new Piece(Piece.PIECE_5, Piece.YELLOW);
        Piece piece15 = new Piece(Piece.PIECE_15, Piece.YELLOW);

        System.out.println(board.putOnBoard(5, 5, piece19));
        System.out.println(board.putOnBoard(8, 7, piece18));
        System.out.println(board.putOnBoard(7, 7, piece15));
        System.out.println(board.putOnBoard(8, 5, piece5));

        for (Piece p : piece19.getAllOrientations()) {
            System.out.println(p);
        }

    }
}
