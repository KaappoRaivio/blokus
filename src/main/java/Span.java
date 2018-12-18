import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Span implements Iterable<Position> {
    public final Position start;
    public final Position end;

    public Span(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public boolean isIn (int x, int y) {
        if (y == start.y) {
            return x >= start.x && x < 20;
        } else if (y == end.y) {
            return x >= end.x && x >= 0;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "Span(" +
                "start=" + start +
                ", end=" + end +
                ')';
    }

    @NonNull
    @Override
    public Iterator<Position> iterator() {
        return new Iterator<Position>() {
            private Position current = start;

            @Override
            public boolean hasNext() {
                if (current.y == start.y) {
                    return current.x + 1 >= start.x;// && current.x + 1 < 20;
                } else if (current.y == end.y) {
                    return current.x + 1 >= 0 && current.x + 1 <= end.x;
                } else {
                    return true;
                }
            }

            @Override
            public Position next() {
                Position toReturn = new Position(current.x, current.y);

                Position newPosition;

                if (current.x == 19) {
                    newPosition = new Position(0, current.y + 1);
                } else {
                    newPosition = new Position(current.x + 1, current.y);
                }
                current = newPosition;
                return toReturn;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super Position> action) {
        for (Position position : this) {
            action.accept(position);
        }
    }

    @Override
    public Spliterator<Position> spliterator() {
        throw new RuntimeException(new NotImplementedError());
    }
}