import java.util.Arrays;
import java.util.List;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board();


        float[] asd = new float[10000];
        for (int i = 0; i < 10000; i++) {
            long alku = System.currentTimeMillis();
            board.getAllFittingMovesParallel(PieceType.BLUE);
//            board.splitBoardInto(4);
//            board.deepCopy();
            long loppu = System.currentTimeMillis();

            asd[i] = (loppu - alku) / 1000f;

        }
        System.out.println(sum(asd) / asd.length);

    }

    public static float sum (float[] floats) {
        float sum = 0;

        for(float float1 : floats) {
            sum += float1;
        }
        return sum;
    }
}
