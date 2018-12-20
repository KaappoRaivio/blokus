
import java.io.*;
import java.util.*;


public class Board implements java.io.Serializable {

    private static final int NO_PIECE = -1;
    private static final int EDGE = -1;


    private int[][] board;
    private int[][] errorBoard;

    private int dimX;
    private int dimY;
    private int amountOfPlayers;

    private List< List<BasePiece> > piecesNotOnBoard = new ArrayList<>();
    private List< List<BasePiece> > piecesOnBoard = new ArrayList<>();

    Board (int dimX, int dimY, int amountOfPlayers, Class<? extends BasePiece> baseBasePiece) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.amountOfPlayers = amountOfPlayers;


        board = new int[dimY][dimX];
        errorBoard = new int[dimY][dimX];

        for (int i = 0; i < amountOfPlayers; i++) {
            try {
                this.piecesNotOnBoard.add((List<BasePiece>) baseBasePiece.getMethod("getAllPieces", int.class).invoke(null, i));
                this.piecesOnBoard.add(new ArrayList<>());
            } catch (Exception ignored) {}
        }

        initializeBoards();

    }

    private void initializeBoards () {
        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                errorBoard[y][x] = NO_PIECE;
                board[y][x] = NO_PIECE;
            }
        }
    }

    private boolean isPieceOnBoard (BasePiece piece) {
        return piece.isOnBoard();
    }

    public boolean putOnBoard(int baseX, int baseY, BasePiece piece) {
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

    private int safeOffset(int baseX, int baseY, int offsetX, int offsetY) {
        try {
            return board[baseY + offsetY][baseX + offsetX];
        } catch (ArrayIndexOutOfBoundsException e) {
            return EDGE;
        }
    }

    private boolean fits (int baseX, int baseY, BasePiece piece) {
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

                if (safeOffset(absX, absY, 0, 0) != NO_PIECE) {
                    fits = false;
                    break;
                }

                if (!touchesCorner &&
                        absX == 0 && absY == 0 ||
                        absX == 0 && absY == dimY - 1 ||
                        absX == dimX - 1 && absY == 0 ||
                        absX == dimX - 1 && absY == dimY - 1) {

                    touchesCorner = true;
                }

                int topRight = safeOffset(absX, absY, +1, -1);
                int topLeft = safeOffset(absX, absY, -1, -1);
                int bottomRight = safeOffset(absX, absY, +1, +1);
                int bottomLeft = safeOffset(absX, absY, -1, +1);


                if (!isConnected &&
                       (topRight == piece.getColor() ||
                        topLeft == piece.getColor() ||
                        bottomRight == piece.getColor() ||
                        bottomLeft == piece.getColor())
                    ) {
                    isConnected = true;
                }

                int top = safeOffset(absX, absY, 0, -1);
                int bottom = safeOffset(absX, absY, 0, +1);
                int left = safeOffset(absX, absY, -1, 0);
                int right = safeOffset(absX, absY, +1, 0);

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

        System.out.println(fits + " " + isConnected);

        if (fits && isConnected) {
            return true;
        } else {
            return false;
        }

    }

    private void addToPiecesOnBoard (BasePiece piece) {
        piecesOnBoard.get(piece.getColor()).add(piece);
        piecesNotOnBoard.get(piece.getColor()).remove(piece);
    }

    private boolean isColorOnBoard (int color) {
        return !piecesOnBoard.get(color).isEmpty();
    }

    private void dummyPut (int baseX, int baseY, BasePiece piece) {
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

    private void errorPut (int baseX, int baseY, BasePiece piece) {
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
        if (color == -1) {
            return Piece.TRANSPARENT;
        }
        return (char) (color + 48);
    }

    private static int getPieceColorFromChar (char color) {
        return ((int) color) - 48;
    }

    public int[][] getBoard() {
        return board;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < dimY; i++) {
            int[] row = board[i];
            builder.append("\n");
            for (int index = 0; index < row.length - 1; index++) {
                if (errorBoard[i][index] != NO_PIECE) {
                    builder.append('E');
                } else {
                    builder.append(getMatchingChar(row[index]));
                }
                builder.append(" ");
            }

            if (errorBoard[i][row.length - 1] != NO_PIECE) {
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

    private List<BasePiece> getPiecesNotOnBoard (int color) {
        return (List<BasePiece>) piecesNotOnBoard.get(color);
    }

    public List<Span> splitBoardInto (int amountOfChunks) {

        int[] lengths = new int[amountOfChunks];
        int remainder = 400 % amountOfChunks;

        for (int i = 0; i < amountOfChunks; i++) {
            lengths[i] = 400 / amountOfChunks;
        }

        for (int i = 0; i < remainder; i++) {
            lengths[i] += 1;
        }

        int startX = 0;
        int startY = 0;

        int endX = 0;
        int endY = 0;

        List<Span> spans = new ArrayList<>();

        for (int length : lengths) {
            endX = (endX + length) % 20;
            endY += (endX + length) / 20;

            spans.add(new Span(new Position(startX, startY), new Position(endX, endY)));

            startX = endX;
            startY = endY;
        }

        return spans;






    }


    public List<Move> getAllFittingMovesParallel(int color) {

        List<Move> result = new ArrayList<>();
        List<Span> spans = splitBoardInto(Main.NUMBER_OF_CORES);
        List<WorkerThread> threads = new ArrayList<>();

        for (Span span : spans) {
            WorkerThread thread = new WorkerThread(this.deepCopy(), span, color);
            threads.add(thread);
            thread.run();

        }

        for (WorkerThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {}

            result.addAll(thread.getResult());
        }


        return result;
    }

    public List<Move> getAllFittingMoves (int color) {
        List<BasePiece> pieces = getPiecesNotOnBoard(color);
        List<Move> moves = new ArrayList<>();

        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                for (BasePiece notRotated : pieces) {
                    for (BasePiece piece : notRotated.getAllOrientations()) {
                        if (fits(x, y, (BasePiece) piece)) {
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

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public class WorkerThread extends Thread {

        private final int color;
        private Board board;
        private Span span;
        private List<Move> moves = new ArrayList<>();

        public WorkerThread (Board board, Span span, int color) {
            super();

            this.board = board;
            this.span = span;
            this.color = color;
        }

        @Override
        public void run() {
            List<BasePiece> pieces = board.getPiecesNotOnBoard(color);

            for (Position position : span) {
                for (BasePiece notRotated : pieces) {
                    for (BasePiece piece : notRotated.getAllOrientations()) {
                        if (board.fits(position.x, position.y, piece)) {
                            moves.add(new Move(position.x, position.y, piece));
                        }
                    }
                }
            }
        }

        public List<Move> getResult () {
            return moves;
        }


    }

}

class NotImplementedError extends Exception {}