import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private short onBoard = 0b0000;

    private List<Piece> bluePiecesOnBoard = new ArrayList<>();
    private List<Piece> redPiecesOnBoard = new ArrayList<>();
    private List<Piece> greenPiecesOnBoard = new ArrayList<>();
    private List<Piece> redPiecesOnBoard = new ArrayList<>();


    private int[][] board;

    Board() {
        this.board = new int[20][20];

        for (int i = 0; i < board.length ; i++) {
            for (int j = 0; j < board[i].length; j++) {
//                board[i][j] = '░';
                board[i][j] = Piece.NO_PIECE;

            }
        }

    }


    public boolean putOnBoard(int baseX, int baseY, Piece piece) {
        if (fits(baseX, baseY, piece)) {
            dummyPut(baseX, baseY, piece);
            return true;
        } else {
            return false;
        }
    }


    private boolean fits (int baseX, int baseY, Piece piece) {
        char[][] mesh = piece.getMesh();

//        char[][] candinate = board.clone();


        boolean isConnected = false;
        boolean fits = true;

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];
                if (current == '.') {
                    continue;
                }

                if (board[y][x] != Piece.NO_PIECE) {
                    fits = false;
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
            setColorOnBoard(piece.getColor());
        }

        System.out.println(isConnected + " " + fits);
        return fits && isConnected;

    }

    private boolean isColorOnBoard (int color) {
        switch (color) {
                case Piece.BLUE:
                    return (onBoard & 1) == 1;
                case Piece.RED:
                    return (onBoard & 2) == 2;
                case Piece.YELLOW:
                    return (onBoard & 4) == 4;
                case Piece.GREEN:
                    return (onBoard & 8) == 8;
                default:
                    throw new RuntimeException("Invalid color " + color + "!");
            }
    }

    private void setColorOnBoard (int color) {
        switch (color) {
            case Piece.BLUE:
                onBoard |= 1;
                break;
            case Piece.RED:
                onBoard |= 2;
                break;
            case Piece.YELLOW:
                onBoard |= 4;
                break;
            case Piece.GREEN:
                onBoard |= 8;
                break;
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
                    case '#':
                        this.board[baseX + y][baseY + x] = piece.getColor();
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

        for (int[] row : board) {
            builder.append("\n");
            for (int index = 0; index < row.length - 1; index++) {
                builder.append(getMatchingChar(row[index]));
                builder.append(" ");
            }

            builder.append(getMatchingChar(row[row.length - 1]));
        }

        return builder.toString();
    }
}
