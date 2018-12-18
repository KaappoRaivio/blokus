
import java.io.*;
import java.util.*;


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

    private PieceType safeOffset(int baseX, int baseY, int offsetX, int offsetY) {
        try {
            return board[baseY + offsetY][baseX + offsetX];
        } catch (ArrayIndexOutOfBoundsException e) {
            return PieceType.EDGE;
        }
    }

    private boolean fits (int baseX, int baseY, Piece piece) {
        char[][] mesh = piece.getMesh();

        if (isPieceOnBoard(piece)) {
            return false;
        }

        boolean isConnected = false;
        boolean fits = true;
        boolean touchesCorner = false;

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];
                if (current == Piece.TRANSPARENT) {
                    continue;
                }

                int absX = baseX + x;
                int absY = baseY + y;

                if (safeOffset(absX, absY, 0, 0) != PieceType.NO_PIECE) {
                    fits = false;
                    break;
                }

                if (!touchesCorner &&
                        absX == 0 && absY == 0 ||
                        absX == 0 && absY == board.length - 1 ||
                        absX == board[absY].length - 1 && absY == 0 ||
                        absX == board[absY].length - 1 && absY == board.length - 1) {

                    touchesCorner = true;
                }

                PieceType topRight = safeOffset(absX, absY, +1, -1);
                PieceType topLeft = safeOffset(absX, absY, -1, -1);
                PieceType bottomRight = safeOffset(absX, absY, +1, +1);
                PieceType bottomLeft = safeOffset(absX, absY, -1, +1);


                if (!isConnected &&
                       (topRight == piece.getColor() ||
                        topLeft == piece.getColor() ||
                        bottomRight == piece.getColor() ||
                        bottomLeft == piece.getColor())
                    ) {
                    isConnected = true;
                }

                PieceType top = safeOffset(absX, absY, 0, -1);
                PieceType bottom = safeOffset(absX, absY, 0, +1);
                PieceType left = safeOffset(absX, absY, -1, 0);
                PieceType right = safeOffset(absX, absY, +1, 0);

                if (top == piece.getColor() ||
                    bottom == piece.getColor() ||
                    left == piece.getColor() ||
                    right == piece.getColor()) {
                        fits = false;
                        break;
                }
            }
        }

        if (fits && !isColorOnBoard(piece.getColor()) && touchesCorner) {
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

    public String save (String name) {
        String path = System.getProperty("user.dir") + "/src/main/resources/boards/" + name + ".ser";


        File file = new File(path);

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

    public String save () {
        return save(String.valueOf(System.currentTimeMillis() * new Random().nextFloat()));
    }

    public static Board fromFile (String path, boolean relative) {
        String absolutePath;

        if (relative) {
            absolutePath = System.getProperty("user.dir") + "/src/main/resources/boards/" + new Date().toString() + ".ser";
        } else {
            absolutePath = path;
        }

        Board board;
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

    public static Board fromHumanReadableFile (String filePath, boolean relative) {
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

    public List<Span> splitBoardInto (int amountOfChunks) {

        List<Span> spans = new ArrayList<>();

        int remainder = 400 % amountOfChunks;
        int step = 400 / amountOfChunks;

        int x = 0;
        int y = 0;

        while (y < board.length) {
            int length;
            System.out.println("" + x + " " + y);
            if (remainder != 0) {
                length = step + 1;
                remainder -= 1;
            } else {
                length = step;
            }

            int newY = length / 20;
            int newX = length % 20;

            spans.add(new Span(new Position(x, y), new Position(x + newX, y + newY)));

            x += newX;
            y += newY;


        }

        return spans;
    }


    public List<Move> getAllFittingMovesParallel() {
        List<Span> spans = splitBoardInto(Main.NUMBER_OF_CORES);
        return null;
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

    public Board deepCopy () {
        Board newBoard;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();

            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            newBoard = (Board) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return newBoard;
    }

//    public static class WorkerThread implements Runnable {
//
//        private Board board;
//        private Span span;
//        private PieceType pieceType;
//        private List<Move> moves = new ArrayList<>();
//
//        public WorkerThread (Board board, Span span, PieceType pieceType) {
//            this.span = span;
//            this.board = board;
//            this.pieceType = pieceType;
//        }
//
//        @Override
//        public void run() {
//            List<Piece> pieces = board.getPiecesNotOnBoard(color);
//
//            for (int y = y1; y < y2; y++) {
//                for (int x = x1; x < x2; x++) {
//                    for (Piece notRotated : pieces) {
//                        for (Piece piece : notRotated.getAllOrientations()) {
//                            if (board.fits(x, y, piece)) {
//                                moves.add(new Move(x, y, piece));
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        public List<Move> getResult () {
//            return moves;
//        }
//
//
//    }

}

class NotImplementedError extends Exception {}