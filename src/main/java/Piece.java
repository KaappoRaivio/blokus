import com.google.common.base.Charsets;

import com.google.common.io.Resources;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Piece implements java.io.Serializable {
    public static final String PIECE_1 = "pieces/piece1.txt";
    public static final String PIECE_2 = "pieces/piece2.txt";
    public static final String PIECE_3 = "pieces/piece3.txt";
    public static final String PIECE_4 = "pieces/piece4.txt";
    public static final String PIECE_5 = "pieces/piece5.txt";
    public static final String PIECE_6 = "pieces/piece6.txt";
    public static final String PIECE_7 = "pieces/piece7.txt";
    public static final String PIECE_8 = "pieces/piece8.txt";
    public static final String PIECE_9 = "pieces/piece9.txt";
    public static final String PIECE_10 = "pieces/piece10.txt";
    public static final String PIECE_11 = "pieces/piece11.txt";
    public static final String PIECE_12 = "pieces/piece12.txt";
    public static final String PIECE_13 = "pieces/piece13.txt";
    public static final String PIECE_14 = "pieces/piece14.txt";
    public static final String PIECE_15 = "pieces/piece15.txt";
    public static final String PIECE_16 = "pieces/piece16.txt";
    public static final String PIECE_17 = "pieces/piece17.txt";
    public static final String PIECE_18 = "pieces/piece18.txt";
    public static final String PIECE_19 = "pieces/piece19.txt";
    public static final String PIECE_20 = "pieces/piece20.txt";
    public static final String PIECE_21 = "pieces/piece21.txt";

    public static final char OPAQUE = '#';
    public static final char TRANSPARENT = '.';

    private char[][] mesh = new char[5][5];
    private PieceType color;

    private int posX = -1;
    private int posY = -1;
    private boolean onBoard = false;

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


    public char[][] getMesh() {
        return mesh;
    }


    private static boolean isValid (PieceType color) {
        switch (color) {
            case BLUE:
                return true;
            case RED:
                return true;
            case GREEN:
                return true;
            case YELLOW:
                return true;
            default:
                return false;
        }
    }

    private void initializeMesh () {
        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                this.mesh[x][y] = Piece.TRANSPARENT;
            }
        }
    }

    public Piece (String filename, PieceType color) {
        if (isValid(color)) {
            this.color = color;
        } else {
            throw new RuntimeException("Invalid color " + color + "!");
        }

        initializeMesh();

        String text = new Scanner(this.getClass().getResourceAsStream(filename), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        String[] lines = text.split("\n");

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                char current = lines[y].charAt(x);
                this.mesh[x][y] = current == Piece.TRANSPARENT || current == Piece.OPAQUE ? current : Piece.TRANSPARENT;
            }
        }

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

    private Piece (char[][] mesh, PieceType color) {
        this.color = color;
        this.mesh = mesh;
    }

    Piece rotate (Orientation orietation, boolean flip) {
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

    public List<Piece> getAllOrientations () {
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

    public static List<Piece> getAllPieces (PieceType color) {
        return Arrays.asList(
                new Piece(PIECE_1, color),
                new Piece(PIECE_2, color),
                new Piece(PIECE_3, color),
                new Piece(PIECE_4, color),
                new Piece(PIECE_5, color),
                new Piece(PIECE_6, color),
                new Piece(PIECE_7, color),
                new Piece(PIECE_8, color),
                new Piece(PIECE_9, color),
                new Piece(PIECE_10, color),
                new Piece(PIECE_11, color),
                new Piece(PIECE_12, color),
                new Piece(PIECE_13, color),
                new Piece(PIECE_14, color),
                new Piece(PIECE_15, color),
                new Piece(PIECE_16, color),
                new Piece(PIECE_17, color),
                new Piece(PIECE_18, color),
                new Piece(PIECE_19, color),
                new Piece(PIECE_20, color),
                new Piece(PIECE_21, color)
        );
    }

    public PieceType getColor() {
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
}