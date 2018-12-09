
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

public class Piece {
    public static final int BLUE = 1;
    public static final int RED = 2;
    public static final int GREEN = 3;
    public static final int YELLOW = 4;
    public static final int NO_PIECE = 0;

    public static String PIECE_1 = "pieces/piece1.txt";
    public static String PIECE_2 = "pieces/piece2.txt";
    public static String PIECE_3 = "pieces/piece3.txt";
    public static String PIECE_4 = "pieces/piece4.txt";
    public static String PIECE_5 = "pieces/piece5.txt";
    public static String PIECE_6 = "pieces/piece6.txt";
    public static String PIECE_7 = "pieces/piece7.txt";
    public static String PIECE_8 = "pieces/piece8.txt";
    public static String PIECE_9 = "pieces/piece9.txt";
    public static String PIECE_10 = "pieces/piece10.txt";
    public static String PIECE_11 = "pieces/piece11.txt";
    public static String PIECE_12 = "pieces/piece12.txt";
    public static String PIECE_13 = "pieces/piece13.txt";
    public static String PIECE_14 = "pieces/piece14.txt";
    public static String PIECE_15 = "pieces/piece15.txt";
    public static String PIECE_16 = "pieces/piece16.txt";
    public static String PIECE_17 = "pieces/piece17.txt";
    public static String PIECE_18 = "pieces/piece18.txt";
    public static String PIECE_19 = "pieces/piece19.txt";
    public static String PIECE_20 = "pieces/piece20.txt";
    public static String PIECE_21 = "pieces/piece21.txt";


    public char[][] getMesh() {
        return mesh;
    }

    private char[][] mesh = new char[5][5];
    private int color;

    Piece (String filename, int color) {
        this.color = color;


        String text = new Scanner(this.getClass().getResourceAsStream(filename), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        String[] lines = text.split("\n");

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                this.mesh[x][y] = lines[y].charAt(x);
            }
        }

    }

    private Piece (char[][] mesh, int color) {
        this.color = color;
        this.mesh = mesh;
    }

    Piece rotate(int orietation, boolean flip) {
        char[][] newlist = new char[5][5];

        switch (orietation) {
            case Orientation.UP:
                newlist = this.mesh;
            case Orientation.DOWN:
                for (int y = 0; y < this.mesh.length; y++) {
                    for (int x = 0; x < this.mesh[y].length; x++) {
                        newlist[4 - y][4 - x] = this.mesh[y][x];
                    }
                }
                break;
            case Orientation.RIGHT:
                for (int y = 0; y < this.mesh.length; y++) {
                    for (int x = 0; x < this.mesh[y].length; x++) {
                        newlist[4 - x][y] = this.mesh[y][x];
                    }
                }
                break;
            case Orientation.LEFT:
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


        return new Piece(afterFlip, this.color);

    }

    public int getColor() {
        return this.color;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder("Color: " + this.color + "\n");
        for (int y = 0; y < this.mesh.length; y++) {
            for (int x = 0; x < this.mesh[y].length; x++) {
                builder.append(this.mesh[x][y]);
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}