import java.util.Arrays;

public class Board {
    private int[][] board;

    public Board() {
        this.board = new int[20][20];
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();

        for (int[] row : board) {
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
