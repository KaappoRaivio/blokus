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
    private BasePiece piece;

    public Move(int x, int y, BasePiece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BasePiece getPiece() {
        return piece;
    }
}
