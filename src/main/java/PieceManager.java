import java.util.List;

public interface PieceManager<T extends BasePiece<T>> {
    public List<T> getCachedPieces (int color);
    public List<PieceID> getPiecesOnBoard (int color);
    public List<PieceID> getPiecesNotOnBoard (int color);
    public T getCachedPiece (PieceID pieceID, int color);
    public void placeOnBoard (PieceID pieceID, int color);
    public boolean isOnBoard (PieceID pieceID, int color);
    public boolean isColorOnBoard (int color);

}
