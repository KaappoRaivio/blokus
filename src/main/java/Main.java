public class Main {
    public static void main (String[] args) {
        Board board = new Board();

        System.out.println(board);

        Piece piece = new Piece(Piece.PIECE_19, Piece.BLUE);

        System.out.println(board.putOnBoard(5, 5, piece));
        System.out.println(board);

    }
}
