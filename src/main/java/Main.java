public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(20, 20, 4, Piece.class);

        board.putOnBoard(0, 0, new Piece(PieceID.PIECE_19, 0).rotate(Orientation.LEFT, false));
        board.putOnBoard(3, 0, new Piece(PieceID.PIECE_14, 0));
        System.out.println(board);


    }

    public static float sum (float[] floats) {
        float sum = 0;

        for(float float1 : floats) {
            sum += float1;
        }
        return sum;
    }
}
