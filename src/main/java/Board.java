import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Piece> bluePiecesOnBoard = new ArrayList<>();
    private List<Piece> redPiecesOnBoard = new ArrayList<>();
    private List<Piece> greenPiecesOnBoard = new ArrayList<>();
    private List<Piece> yellowPiecesOnBoard = new ArrayList<>();


    private int[][] board;
    private int[][] errorBoard;

    Board() {
        board = new int[20][20];
        errorBoard = new int[20][20];

        for (int y = 0; y < board.length ; y++) {
            for (int x = 0; x < board[y].length; x++) {
                errorBoard[y][x] = Piece.NO_PIECE;
                board[y][x] = Piece.NO_PIECE;
            }
        }
    }

    private boolean isPieceOnBoard (Piece piece) {
        switch (piece.getColor()) {
            case Piece.BLUE:
                return bluePiecesOnBoard.contains(piece);
            case Piece.RED:
                return redPiecesOnBoard.contains(piece);
            case Piece.YELLOW:
                return yellowPiecesOnBoard.contains(piece);
            case Piece.GREEN:
                return greenPiecesOnBoard.contains(piece);
            default:
                throw new RuntimeException("Invalid color " + piece.getColor() + "!");
        }
    }


    public boolean putOnBoard(int baseX, int baseY, Piece piece) {
        if (fits(baseX, baseY, piece)) {
            this.dummyPut(baseX, baseY, piece);
            piece.placeOnBoard(baseX, baseY);
            return true;
        } else {
            errorPut(baseX, baseY, piece);
            return false;
        }
    }


    private boolean fits (int baseX, int baseY, Piece piece) {
        char[][] mesh = piece.getMesh();

        if (isPieceOnBoard(piece)) {
            return false;
        }

        boolean isConnected = false;
        boolean fits = true;

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];

                if (current == Piece.TRANSPARENT) {
                    continue;
                }

                if (board[y][x] != Piece.NO_PIECE) {
                    fits =
                            false;
                    break;
                }

                if (!isConnected &&
                       (board[y + baseY + 1][x + baseX + 1] == piece.getColor() ||
                        board[y + baseY + 1][x + baseX - 1] == piece.getColor() ||
                        board[y + baseY - 1][x + baseX - 1] == piece.getColor() ||
                        board[y + baseY - 1][x + baseX + 1] == piece.getColor())
                    ) {
                    isConnected = true;
                }

                if (board[y + baseY][x + baseX + 1] == piece.getColor() ||
                    board[y + baseY][x + baseX - 1] == piece.getColor() ||
                    board[y + baseY + 1][x + baseX] == piece.getColor() ||
                    board[y + baseY - 1][x + baseX] == piece.getColor()) {
                    fits = false;
                    break;
                }
            }
        }

        if (fits && !isColorOnBoard(piece.getColor())) {
            isConnected = true;
        }


        if (fits && isConnected) {
            addToPiecesOnBoard(piece);
            return true;
        } else {
            return false;
        }

    }

    private void addToPiecesOnBoard (Piece piece) {
        switch (piece.getColor()) {
            case Piece.BLUE:
                bluePiecesOnBoard.add(piece);
                break;
            case Piece.RED:
                redPiecesOnBoard.add(piece);
                break;
            case Piece.YELLOW:
                yellowPiecesOnBoard.add(piece);
                break;
            case Piece.GREEN:
                greenPiecesOnBoard.add(piece);
                break;

        }
    }

    private boolean isColorOnBoard (int color) {
        switch (color) {
            case Piece.BLUE:
                return !bluePiecesOnBoard.isEmpty();
            case Piece.RED:
                return !redPiecesOnBoard.isEmpty();
            case Piece.YELLOW:
                return !yellowPiecesOnBoard.isEmpty();
            case Piece.GREEN:
                return !greenPiecesOnBoard.isEmpty();
            default:
                throw new RuntimeException("Invalid color " + color + "!");
        }
    }


    private void dummyPut (int baseX, int baseY, Piece piece) {
        char[][] mesh = piece.getMesh();

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];

                switch (current) {
                    case Piece.OPAQUE:
                        this.board[baseY + y][baseX + x] = piece.getColor();
                        break;
                    case Piece.TRANSPARENT:
                        break;
                    default:
                        throw new RuntimeException("Invalid piece " + piece.toString() + ", " + current);
                }

            }
        }
    }

    private void errorPut (int baseX, int baseY, Piece piece) {
        char[][] mesh = piece.getMesh();

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];

                switch (current) {
                    case Piece.OPAQUE:
                        errorBoard[baseY + y][baseX + x] = piece.getColor();
                        break;
                    case Piece.TRANSPARENT:
                        break;
                    default:
                        throw new RuntimeException("Invalid piece " + piece.toString());
                }

            }
        }
    }


    private static char getMatchingChar (int color) {
        switch (color) {
            case Piece.BLUE:
                return 'B';
            case Piece.RED:
                return 'R';
            case Piece.GREEN:
                return 'G';
            case Piece.YELLOW:
                return 'Y';
            case Piece.NO_PIECE:
                return '░';
            default:
                throw new RuntimeException("Invalid color " + color + "!");
        }
    }


    public int[][] getBoard() {
        return board;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            builder.append("\n");
            for (int index = 0; index < row.length - 1; index++) {
                if (errorBoard[i][index] != Piece.NO_PIECE) {
                    builder.append('E');
                } else {
                    builder.append(getMatchingChar(row[index]));
                }
                builder.append(" ");
            }

            if (errorBoard[i][row.length - 1] != Piece.NO_PIECE) {
                builder.append('E');
            } else {
                builder.append(getMatchingChar(row[row.length - 1]));
            }

        }

        return builder.toString();
    }
}
