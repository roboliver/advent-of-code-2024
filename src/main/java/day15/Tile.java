package day15;

import java.util.Arrays;
import java.util.List;

public enum Tile {
    BOX('O'),
    WALL('#'),
    ROBOT('@'),
    EMPTY('.'),
    LEFT_BOX('['),
    RIGHT_BOX(']');

    private final char asChar;

    Tile(char asChar) {
        this.asChar = asChar;
    }

    public static Tile parse(char c) {
        return Arrays.stream(values())
                .filter(it -> it.asChar == c)
                .findFirst()
                .orElseThrow();
    }

    public static List<Tile> parseWide(char c) {
        return switch (c) {
            case 'O' -> List.of(Tile.LEFT_BOX, Tile.RIGHT_BOX);
            case '#' -> List.of(Tile.WALL, Tile.WALL);
            case '@' -> List.of(Tile.ROBOT, Tile.EMPTY);
            case '.' -> List.of(Tile.EMPTY, Tile.EMPTY);
            default -> throw new IllegalArgumentException("can't parse " + c);
        };
    }

    @Override
    public String toString() {
        return Character.toString(asChar);
    }
}
