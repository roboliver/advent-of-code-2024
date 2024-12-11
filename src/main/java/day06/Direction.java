package day06;

import common.Point;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public enum Direction {
    UP('^', it -> new Point(it.x(), it.y() - 1)),
    RIGHT('>', it -> new Point(it.x() + 1, it.y())),
    DOWN('v', it -> new Point(it.x(), it.y() + 1)),
    LEFT('<', it -> new Point(it.x() - 1, it.y()));

    private final char guardChar;
    private final UnaryOperator<Point> moveFunc;

    Direction(char guardChar, UnaryOperator<Point> moveFunc) {
        this.guardChar = guardChar;
        this.moveFunc = moveFunc;
    }

    Point move(Point cur) {
        return moveFunc.apply(cur);
    }

    char getGuardChar() {
        return this.guardChar;
    }

    static boolean isGuard(char tile) {
        return Arrays.stream(values())
                .anyMatch(it -> it.guardChar == tile);
    }

    static Direction fromChar(char tile) {
        return Arrays.stream(values())
                .filter(it -> it.guardChar == tile)
                .findFirst()
                .orElseThrow();
    }

    static Direction rotate(Direction from) {
        return switch(from) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }
}