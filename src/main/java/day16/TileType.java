package day16;

import java.util.Arrays;

public enum TileType {
    WALL('#'),
    PATH('.'),
    START('S'),
    END('E');

    private final char asChar;

    TileType(char asChar) {
        this.asChar = asChar;
    }

    public static TileType parse(char c) {
        return Arrays.stream(values())
                .filter(it -> it.asChar == c)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public String toString() {
        return Character.toString(asChar);
    }
}
