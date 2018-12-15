public class Move implements java.io.Serializable {
    @Override
    public String toString() {
        return "Move(" +
                "x=" + x +
                ", y=" + y +
                ", piece=" + piece +
                ')';
    }

    private int x;
    private int y;

    public Move(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    private Piece piece;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Piece getPiece() {
        return piece;
    }
}
