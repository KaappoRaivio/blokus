import javax.management.relation.RoleUnresolved;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class MyPieceManager<T extends BasePiece<T>> implements PieceManager<T>, java.io.Serializable {
    private List< List<T> > cachedPieces = new ArrayList<>();
    private List< List<PieceID> > piecesOnBoard = new ArrayList<>();
    private List< List<PieceID> > piecesNotOnBoard = new ArrayList<>();

//    private boolean[] piecesOnBoard;

    public MyPieceManager (int amountOfColors) {
        for (int i = 0; i < amountOfColors; i++) {
            piecesNotOnBoard.add(new ArrayList<>());
            piecesOnBoard.add(new ArrayList<>());
            cachedPieces.add(new ArrayList<>());

            List<PieceID> pieceIDs = T.getAllPieces(i);
            for (PieceID pieceID : pieceIDs) {
                try {
                    cachedPieces.get(i).add((T) new Piece(pieceID, i));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            piecesNotOnBoard.get(i).addAll(pieceIDs);
        }
    }

    @Override
    public List<T> getCachedPieces(int color) {
        return cachedPieces.get(color);
    }

    @Override
    public List<PieceID> getPiecesOnBoard(int color) {
        return piecesOnBoard.get(color);
    }

    @Override
    public List<PieceID> getPiecesNotOnBoard(int color) {
        return piecesNotOnBoard.get(color);
    }

    @Override
    public T getCachedPiece(PieceID pieceID, int color) {
        return cachedPieces.get(color).get(pieceID.ordinal());
    }

    @Override
    public void placeOnBoard(PieceID pieceID, int color) {
        if (isOnBoard(pieceID, color)) {
            throw new RuntimeException("Piece is already on board!");
        }

        piecesNotOnBoard.get(color).remove(pieceID);
        piecesOnBoard.get(color).add(pieceID);
    }

    @Override
    public boolean isOnBoard (PieceID pieceID, int color) {
        return piecesOnBoard.get(color).contains(pieceID);
    }

    @Override
    public boolean isColorOnBoard(int color) {
        return !piecesOnBoard.get(color).isEmpty();
    }
}
