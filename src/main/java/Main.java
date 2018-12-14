public class Main {
    public static void main (String[] args) {
//        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/Fri Dec 14 14:26:31 CET 2018.ser", false);
        Board board = new Board();

        Piece piece19 = new Piece(Piece.PIECE_19, PieceType.BLUE);
        Piece piece18 = new Piece(Piece.PIECE_18, PieceType.BLUE).rotate(Orientation.DOWN, false);
        Piece piece5 = new Piece(Piece.PIECE_5, PieceType.YELLOW);
        Piece piece15 = new Piece(Piece.PIECE_15, PieceType.YELLOW);

        System.out.println(board.putOnBoard(5, 5, piece19));
        System.out.println(board.putOnBoard(8, 7, piece18));
        System.out.println(board.putOnBoard(7, 7, piece15));
        System.out.println(board.putOnBoard(8, 5, piece5));

        System.out.println(board);

//        String path = board.save();
//        System.out.println(path);

        for (Piece p : piece19.getAllOrientations()) {
            System.out.println(p);
        }

    }
}
