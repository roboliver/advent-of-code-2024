package day14;

import common.Point;
import config.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DayFourteen implements Day<Integer> {

    private final List<PosVel> posVels;
    private final int width;
    private final int height;

    public DayFourteen(BufferedReader input) throws IOException {
        this(input, 101, 103);
    }

    public DayFourteen(BufferedReader input, int width, int height) throws IOException {
        try (input) {
            posVels = input.lines()
                    .map(line -> {
                        var parts = line.split(" ");
                        var position = parsePart(parts[0]);
                        var velocity = parsePart(parts[1]);
                        return new PosVel(position.x(), position.y(), velocity.x(), velocity.y());
                    })
                    .toList();
        }
        this.width = width;
        this.height = height;
    }

    private Point parsePart(String part) {
        var pair = part.substring(2).split(",");
        return new Point(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
    }

    @Override
    public String partOneName() {
        return "safety factor after 100 seconds";
    }

    @Override
    public Integer partOne() {
        var robots = Arrays.stream(Quadrant.values())
                .filter(it -> it != Quadrant.NONE)
                .collect(Collectors.toMap(Function.identity(), it -> 0));
        for (var posVel : posVels) {
            int x = calcPositionAfter(posVel.xPos(), posVel.xVel(), width, 100);
            int y = calcPositionAfter(posVel.yPos(), posVel.yVel(), height, 100);
            var quadrant = determineQuadrant(x, y);
            if (quadrant != Quadrant.NONE) {
                robots.merge(quadrant, 1, Integer::sum);
            }
        }
        return robots.values().stream()
                .reduce(1, (a, b) -> a * b);
    }

    @Override
    public String partTwoName() {
        return "seconds for first Christmas tree";
    }

    @Override
    public Integer partTwo() {
        int horizontalBandOffset = -1;
        int verticalBandOffset = -1;
        for (int i = 0; i < Math.max(width, height) && (horizontalBandOffset == -1 || verticalBandOffset == -1); i++) {
            var room = calcRoomAfter(i);
            if (aligned(room, room.length, this::robotCountRow)) {
                horizontalBandOffset = i;
            }
            if (aligned(room, room[0].length, this::robotCountCol)) {
                verticalBandOffset = i;
            }
        }
        if (horizontalBandOffset == -1 || verticalBandOffset == -1) {
            throw new IllegalStateException();
        }
        return findOverlap(horizontalBandOffset, verticalBandOffset);
    }

    private int findOverlap(int horizontalBandOffset, int verticalBandOffset) {
        int factorFromOffset = verticalBandOffset - horizontalBandOffset;
        int factorFromInverse = -1;
        for (int i = 0; i < height; i++) {
            if ((i * height) % width == 1) {
                factorFromInverse = i;
            }
        }
        if (factorFromInverse == -1) {
            throw new IllegalStateException();
        }
        int multiple = (factorFromOffset * factorFromInverse) % width;
        return multiple * height + horizontalBandOffset;
    }

    private boolean aligned(int[][] room, int length, BiFunction<int[][], Integer, Integer> countFunc) {
        int bandWidth = length / 3;
        int robots = 0;
        for (int i = 0; i < bandWidth; i++) {
            robots += countFunc.apply(room, i);
        }
        for (int i = bandWidth; i < length; i++) {
            if (isBand(robots)) {
                return true;
            }
            robots -= countFunc.apply(room, i - bandWidth);
            robots += countFunc.apply(room, i);
        }
        return isBand(robots);
    }

    private int robotCountRow(int[][] room, int row) {
        return Arrays.stream(room[row])
                .sum();
    }

    private int robotCountCol(int[][] room, int col) {
        return Arrays.stream(room)
                .mapToInt(it -> it[col])
                .sum();
    }

    private boolean isBand(int robots) {
        return robots > posVels.size() * 0.75;
    }

    private Quadrant determineQuadrant(int x, int y) {
        if (x < width / 2 && y < height / 2) {
            return Quadrant.UPPER_LEFT;
        } else if (x < width / 2 && y > height / 2) {
            return Quadrant.LOWER_LEFT;
        } else if (x > width / 2 && y < height / 2) {
            return Quadrant.UPPER_RIGHT;
        } else if (x > width / 2 && y > height / 2) {
            return Quadrant.LOWER_RIGHT;
        } else {
            return Quadrant.NONE;
        }
    }

    private int[][] calcRoomAfter(int seconds) {
        int[][] room = new int[height][width];
        for (var posVel : posVels) {
            int x = calcPositionAfter(posVel.xPos(), posVel.xVel(), width, seconds);
            int y = calcPositionAfter(posVel.yPos(), posVel.yVel(), height, seconds);
            room[y][x]++;
        }
        return room;
    }

    private String displayRoomAfter(int seconds) {
        int[][] room = calcRoomAfter(seconds);
        var buf = new StringBuilder();
        for (int y = 0; y < room.length; y++) {
            for (int x = 0; x < room[0].length; x++) {
                if (room[y][x] == 0) {
                    buf.append('.');
                } else {
                    buf.append(room[y][x]);
                }
            }
            buf.append("\n");
        }
        return buf.toString();
    }

    private int calcPositionAfter(int position, int velocity, int roomSize, int seconds) {
        return (position + (seconds * velocity) % roomSize + roomSize) % roomSize;
    }

}
