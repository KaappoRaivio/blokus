import java.util.List;

public abstract class BasePiece<T extends BasePiece> {
    abstract T rotate(Orientation orietation, boolean flip);
    abstract void placeOnBoard(int posX, int posY);
    abstract public int getColor();
    abstract public String toString ();
    abstract public boolean isOnBoard ();
    abstract public void setOnBoard (boolean set);
    abstract public char[][] getMesh ();
    abstract public List<T> getAllOrientations ();
    abstract public PieceID getID ();
    abstract public Orientation getOrientation ();
    abstract public boolean isFlipped ();
    public static List<PieceID> getAllPieces (int pieceColor) {
        return Piece.getAllPieces(pieceColor);
    }

    public static int amountOfUniquePieces () {
        return Piece.amountOfUniquePieces();
    }



}
