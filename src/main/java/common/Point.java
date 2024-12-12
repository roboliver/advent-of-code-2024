package common;

public record Point(int x, int y) {

    public static final Point ORIGIN = new Point(0, 0);

    public enum Turn {
        QUARTER, HALF, THREE_QUARTER
    }

    public Point plus(Point other) {
        return new Point(x + other.x, y + other.y);
    }

    public Point minus(Point other) {
        return new Point(x - other.x, y - other.y);
    }

    public Point rotate(Turn turn) {
        return rotateAround(ORIGIN, turn);
    }

    public Point rotateAround(Point other, Turn turn) {
        return switch(turn) {
            case QUARTER -> new Point(y, 2 * other.x() - x);
            case HALF -> new Point(2 * other.x() - x, 2 * other.y() - y);
            case THREE_QUARTER -> new Point(2 * other.y() - y, x);
        };
    }


}
