public class Span {
    public final Position start;
    public final Position end;

    public Span(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public boolean isIn (int x, int y) {
        return x >= start.x && x < end.x && y >= start.y && y < end.y;
    }

    @Override
    public String toString() {
        return "Span(" +
                "start=" + start +
                ", end=" + end +
                ')';
    }
}