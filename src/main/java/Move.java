import java.util.Optional;

public class Move implements java.io.Serializable {
    @Override
    public String toString() {
        return "Move{" +
                "x=" + x +
                ", y=" + y +
                ", pieceID=" + pieceID +
                ", color=" + color +
                ", orientation=" + orientation +
                ", flip=" + flip +
                '}';
    }

    private int x;
    private int y;
    private PieceID pieceID;
    private int color;
    private Orientation orientation;
    private boolean flip;

    public Move(int x, int y, PieceID pieceID, int color, Orientation orientation, boolean flip) {
        this.x = x;
        this.y = y;
        this.pieceID = pieceID;
        this.color = color;
        this.orientation = orientation;
        this.flip = flip;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PieceID getPieceID() {
        return pieceID;
    }

    public int getColor() {
        return color;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isFlip() {
        return flip;
    }
}
