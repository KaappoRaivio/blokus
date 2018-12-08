import java.util.Arrays;

public class Board {
    private boolean redOnBoard = false;
    private boolean blueOnBoard = false;
    private boolean greenOnBoard = false;
    private boolean yellowOnBoard = false;


    private char[][] board;

    Board() {
        this.board = new char[20][20];

        for (int i = 0; i < board.length ; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = '░';
            }
        }

    }





    public boolean fits (int baseX, int baseY, Piece piece) {
        char[][] mesh = piece.getMesh();

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];


            }
        }

        return false;

    }

    private void dummyPut (int baseX, int baseY, Piece piece) {
        char[][] mesh = piece.getMesh();

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];

                switch (current) {
                    case '#':
                        this.board[baseX + y][baseY + x] = getMatchingChar(piece.getColor());
                        break;
                    case '.':
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
            default:
                return 'A';
        }
    }


    public char[][] getBoard() {
        return board;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();

        for (char[] row : board) {
            builder.append("\n");
            for (int index = 0; index < row.length - 1; index++) {
                builder.append(row[index]);
                builder.append(" ");
            }

            builder.append(row[row.length - 1]);
        }

        return builder.toString();
    }
}
