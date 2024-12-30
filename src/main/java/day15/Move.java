package day15;

import java.util.Arrays;

public enum Move {
    UP('^'),
    DOWN('v'),
    RIGHT('>'),
    LEFT('<');

    private final char asChar;

    Move(char asChar) {
        this.asChar = asChar;
    }

    public static Move parse(char c) {
        return Arrays.stream(values())
                .filter(it -> it.asChar == c)
                .findFirst()
                .orElseThrow();
    }

    public static Move reverse(Move move) {
        return switch (move) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
