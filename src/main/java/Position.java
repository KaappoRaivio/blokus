public class Position {
    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position(" +
                "x=" + x +
                ", y=" + y +
                ')';
    }
}
