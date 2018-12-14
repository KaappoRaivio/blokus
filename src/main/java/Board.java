
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Board implements java.io.Serializable {

    private List<Piece> bluePiecesOnBoard = new ArrayList<>();
    private List<Piece> redPiecesOnBoard = new ArrayList<>();
    private List<Piece> greenPiecesOnBoard = new ArrayList<>();
    private List<Piece> yellowPiecesOnBoard = new ArrayList<>();

    private List<Piece> bluePiecesNotOnBoard = Piece.getAllPieces(PieceType.BLUE);
    private List<Piece> redPiecesNotOnBoard = Piece.getAllPieces(PieceType.RED);
    private List<Piece> greenPiecesNotOnBoard = Piece.getAllPieces(PieceType.GREEN);
    private List<Piece> yellowPiecesNotOnBoard = Piece.getAllPieces(PieceType.YELLOW);


    private PieceType[][] board;
    private PieceType[][] errorBoard;

    Board () {
        board = new PieceType[20][20];
        errorBoard = new PieceType[20][20];

        initializeBoards();
    }

    private void initializeBoards () {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                errorBoard[y][x] = PieceType.NO_PIECE;
                board[y][x] = PieceType.NO_PIECE;
            }
        }
    }



    private boolean isPieceOnBoard (Piece piece) {
        switch (piece.getColor()) {
            case BLUE:
                return bluePiecesOnBoard.contains(piece);
            case RED:
                return redPiecesOnBoard.contains(piece);
            case YELLOW:
                return yellowPiecesOnBoard.contains(piece);
            case GREEN:
                return greenPiecesOnBoard.contains(piece);
            default:
                throw new RuntimeException("Invalid color " + piece.getColor() + "!");
        }
    }


    public boolean putOnBoard(int baseX, int baseY, Piece piece) {
        if (fits(baseX, baseY, piece)) {
            dummyPut(baseX, baseY, piece);
            addToPiecesOnBoard(piece);
            piece.placeOnBoard(baseX, baseY);
            return true;
        } else {
            errorPut(baseX, baseY, piece);
            return false;
        }
    }

    public boolean putOnBoard (Move move) {
        return putOnBoard(move.getX(), move.getY(), move.getPiece());
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

                if (board[y][x] != PieceType.NO_PIECE) {
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
            return true;
        } else {
            return false;
        }

    }

    private void addToPiecesOnBoard (Piece piece) {
        switch (piece.getColor()) {
            case BLUE:
                bluePiecesOnBoard.add(piece);
                bluePiecesNotOnBoard.remove(piece);
                break;
            case RED:
                redPiecesOnBoard.add(piece);
                redPiecesNotOnBoard.remove(piece);
                break;
            case YELLOW:
                yellowPiecesOnBoard.add(piece);
                yellowPiecesNotOnBoard.remove(piece);
                break;
            case GREEN:
                greenPiecesOnBoard.add(piece);
                greenPiecesNotOnBoard.remove(piece);
                break;

        }
    }

    private boolean isColorOnBoard (PieceType color) {
        switch (color) {
            case BLUE:
                return !bluePiecesOnBoard.isEmpty();
            case RED:
                return !redPiecesOnBoard.isEmpty();
            case YELLOW:
                return !yellowPiecesOnBoard.isEmpty();
            case GREEN:
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


    private static char getMatchingChar (PieceType color) {
        switch (color) {
            case BLUE:
                return 'B';
            case RED:
                return 'R';
            case GREEN:
                return 'G';
            case YELLOW:
                return 'Y';
            case NO_PIECE:
                return '░';
            default:
                throw new RuntimeException("Invalid color " + color + "!");
        }
    }

    private static PieceType getPieceColorFromChar (char color) {
        switch (color) {
            case 'B':
                return PieceType.BLUE;
            case 'R':
                return PieceType.RED;
            case 'Y':
                return PieceType.YELLOW;
            case 'G':
                return PieceType.GREEN;
            case '░':
                return PieceType.NO_PIECE;
            default:
                throw new RuntimeException("Invalid color " + color + "!");
        }
    }


    public PieceType[][] getBoard() {
        return board;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            PieceType[] row = board[i];
            builder.append("\n");
            for (int index = 0; index < row.length - 1; index++) {
                if (errorBoard[i][index] != PieceType.NO_PIECE) {
                    builder.append('E');
                } else {
                    builder.append(getMatchingChar(row[index]));
                }
                builder.append(" ");
            }

            if (errorBoard[i][row.length - 1] != PieceType.NO_PIECE) {
                builder.append('E');
            } else {
                builder.append(getMatchingChar(row[row.length - 1]));
            }

        }

        return builder.toString();
    }

    public String save() {
        String path = System.getProperty("user.dir") + "/src/main/resources/boards/" + new Date().toString() + ".ser";


        File file = new File(path);
        System.out.println(file.exists());

        try {
            if (file.createNewFile()) {
                System.out.println("Creating new file " + path);

            } else {
                System.out.println("File " + path + " already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            FileOutputStream fileOut = new FileOutputStream(path);

            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();

            fileOut.close();

            System.out.println("Saved board to: " + path);
      } catch (IOException e) {
            throw new RuntimeException(e);
      }

      return path;
    }

    public static Board fromFile (String path, boolean relative) {
        String absolutePath;

        if (relative) {
            absolutePath = System.getProperty("user.dir") + "/src/main/resources/boards/" + new Date().toString() + ".ser";
        } else {
            absolutePath = path;
        }

        Board board = null;
        try {
            FileInputStream fileIn = new FileInputStream(absolutePath);

            ObjectInputStream in = new ObjectInputStream(fileIn);
            board = (Board) in.readObject();
            in.close();

            fileIn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return board;
    }

    public static Board fromHumanReadableFile (String filePath, boolean absolute) {
        throw new RuntimeException(new NotImplementedError());
    }

    private List<Piece> getPiecesNotOnBoard (PieceType color) {
        switch (color) {
            case BLUE:
                return bluePiecesNotOnBoard;
            case RED:
                return redPiecesNotOnBoard;
            case GREEN:
                return greenPiecesNotOnBoard;
            case YELLOW:
                return yellowPiecesNotOnBoard;
            default:
                throw new RuntimeException("Invalid color " + color);
        }
    }

    public List<Move> getAllFittingMoves (PieceType color) {
        List<Piece> pieces = getPiecesNotOnBoard(color);
        List<Move> moves = new ArrayList<>();

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                for (Piece notRotated : pieces) {
                    for (Piece piece : notRotated.getAllOrientations()) {
                        if (fits(x, y, piece)) {
                            moves.add(new Move(x, y, piece));
                        }
                    }
                }
            }
        }

        return moves;
    }





}


class NotImplementedError extends Exception {}
