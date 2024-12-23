package day12;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction reverse(Direction direction) {
        return switch (direction) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
