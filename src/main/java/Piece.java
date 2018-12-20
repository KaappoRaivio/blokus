import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Piece extends BasePiece implements java.io.Serializable {
    private static final List<String> paths = Arrays.asList(
            "pieces/piece1.txt",
            "pieces/piece2.txt",
            "pieces/piece3.txt",
            "pieces/piece4.txt",
            "pieces/piece5.txt",
            "pieces/piece6.txt",
            "pieces/piece7.txt",
            "pieces/piece8.txt",
            "pieces/piece9.txt",
            "pieces/piece10.txt",
            "pieces/piece11.txt",
            "pieces/piece12.txt",
            "pieces/piece13.txt",
            "pieces/piece14.txt",
            "pieces/piece15.txt",
            "pieces/piece16.txt",
            "pieces/piece17.txt",
            "pieces/piece18.txt",
            "pieces/piece19.txt",
            "pieces/piece20.txt",
            "pieces/piece21.txt"
    );


    public static final char OPAQUE = '#';
    public static final char TRANSPARENT = '.';

    private char[][] mesh = new char[5][5];
    private int color;

    private int posX = -1;
    private int posY = -1;
    private boolean onBoard = false;
    private PieceID id;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }


    public void placeOnBoard (int posX, int posY) {
        if (onBoard) {
            throw new RuntimeException("Piece " + this.toString() + " is already on board at coordnates " + this.posX + ", " + this.posY + "!");
        }

        onBoard = true;
        this.posX = posX;
        this.posY = posY;
    }

    public PieceID getID () {
        return id;
    }

    public char[][] getMesh() {
        return mesh;
    }


    private static boolean isValid (int color) {
        return true;
    }

    private void initializeMesh () {
        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                this.mesh[x][y] = Piece.TRANSPARENT;
            }
        }
    }

    public Piece (PieceID pieceID, int color) {
        if (isValid(color)) {
            this.color = color;
        } else {
            throw new RuntimeException("Invalid color " + color + "!");
        }

        initializeMesh();

        String text = new Scanner(this.getClass().getResourceAsStream(paths.get(pieceID.ordinal())), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        String[] lines = text.split("\n");

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                char current = lines[y].charAt(x);
                this.mesh[x][y] = current == Piece.TRANSPARENT || current == Piece.OPAQUE ? current : Piece.TRANSPARENT;
            }
        }

        this.id = pieceID;

    }

    private void moveLeft () {
        for (int y = 0; y < 5; y++) {
            if (mesh[0][y] == Piece.OPAQUE) {
                return;
            }
        }

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x + 1 >= 5) {
                    mesh[x][y] = Piece.TRANSPARENT;
                } else {
                    mesh[x][y] = mesh[x + 1][y];
                }
            }
        }

        moveLeft();
    }

    private void moveUp () {
        for (int x = 0; x < 5; x++) {
            if (mesh[x][0] == Piece.OPAQUE) {
                return;
            }
        }

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (y >= 4) {
                    mesh[x][y] = Piece.TRANSPARENT;
                } else {
                    mesh[x][y] = mesh[x][y + 1];
                }
            }
        }

        moveUp();
    }

    private Piece (char[][] mesh, int color) {
        this.color = color;
        this.mesh = mesh;
    }

    public Piece rotate (Orientation orietation, boolean flip) {
        char[][] newlist = new char[5][5];

        switch (orietation) {
            case UP:
                newlist = this.mesh;
                break;
            case DOWN:
                for (int y = 0; y < this.mesh.length; y++) {
                    for (int x = 0; x < this.mesh[y].length; x++) {
                        newlist[4 - y][4 - x] = this.mesh[y][x];
                    }
                }
                break;
            case RIGHT:
                for (int y = 0; y < this.mesh.length; y++) {
                    for (int x = 0; x < this.mesh[y].length; x++) {
                        newlist[4 - x][y] = this.mesh[y][x];
                    }
                }
                break;
            case LEFT:
                for (int y = 0; y < this.mesh.length; y++) {
                    for (int x = 0; x < this.mesh[y].length; x++) {
                        newlist[x][4 - y] = this.mesh[y][x];
                    }
                }
                break;
        }

        char[][] afterFlip = new char[5][5];

        if (flip) {
            for (int y = 0; y < this.mesh.length; y++) {
                for (int x = 0; x < this.mesh[y].length; x++) {
                    afterFlip[y][4 - x] = newlist[y][x];
                }
            }
        } else {
            afterFlip = newlist;
        }
        Piece piece = new Piece(afterFlip, this.color);
        piece.moveLeft();
        piece.moveUp();

        return piece;

    }

    public List<BasePiece> getAllOrientations () {
        return Arrays.asList(
                rotate(Orientation.UP, false),
                rotate(Orientation.RIGHT, false),
                rotate(Orientation.DOWN, false),
                rotate(Orientation.LEFT, false),
                rotate(Orientation.UP, true),
                rotate(Orientation.RIGHT, true),
                rotate(Orientation.DOWN, true),
                rotate(Orientation.LEFT, true)
        );
    }

    public static List<BasePiece> getAllPieces (int color) {
        return Arrays.asList(
                new Piece(PieceID.PIECE_1, color),
                new Piece(PieceID.PIECE_2, color),
                new Piece(PieceID.PIECE_3, color),
                new Piece(PieceID.PIECE_4, color),
                new Piece(PieceID.PIECE_5, color),
                new Piece(PieceID.PIECE_6, color),
                new Piece(PieceID.PIECE_7, color),
                new Piece(PieceID.PIECE_8, color),
                new Piece(PieceID.PIECE_9, color),
                new Piece(PieceID.PIECE_10, color),
                new Piece(PieceID.PIECE_11, color),
                new Piece(PieceID.PIECE_12, color),
                new Piece(PieceID.PIECE_13, color),
                new Piece(PieceID.PIECE_14, color),
                new Piece(PieceID.PIECE_15, color),
                new Piece(PieceID.PIECE_16, color),
                new Piece(PieceID.PIECE_17, color),
                new Piece(PieceID.PIECE_18, color),
                new Piece(PieceID.PIECE_19, color),
                new Piece(PieceID.PIECE_20, color),
                new Piece(PieceID.PIECE_21, color)
        );
    }

    public int getColor() {
        return this.color;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder("Color: " + this.color + "\n");
        for (int y = 0; y < this.mesh.length; y++) {
            for (int x = 0; x < this.mesh[y].length; x++) {
                builder.append(this.mesh[x][y] == Piece.TRANSPARENT ? " " : this.mesh[x][y]);
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public boolean isOnBoard() {
        return onBoard;
    }

    @Override
    public void setOnBoard(boolean set) {
        this.onBoard = set;
    }
}