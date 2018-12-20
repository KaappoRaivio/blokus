import java.util.List;

public abstract class BasePiece {
    abstract Piece rotate(Orientation orietation, boolean flip);
    abstract void placeOnBoard(int posX, int posY);
    abstract public int getColor();
    abstract public String toString ();
    abstract public boolean isOnBoard ();
    abstract public void setOnBoard (boolean set);
    abstract public char[][] getMesh ();
    abstract public List<BasePiece> getAllOrientations ();
//    abstract public List<BasePiece> getAllPieces (int pieceColor);

    public static List<BasePiece> getAllPieces (int pieceColor) {
        throw new RuntimeException(new NotImplementedError());
    }

}
