import java.util.List;

public interface PieceManager {
    public List<Piece> getCachedPieces (int color);
    public List<PieceID> getPiecesOnBoard (int color);
    public List<PieceID> getPiecesNotOnBoard (int color);
    public Piece getCachedPiece (PieceID pieceID, int color);
    public void placeOnBoard (PieceID pieceID, int color);
    public boolean isOnBoard (PieceID pieceID, int color);
    public boolean isColorOnBoard (int color);
    public int getAmountOfPlayers ();

}
