package day18;

import config.DaySplitType;
import common.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DayEighteen implements DaySplitType<Integer, String> {

    private final List<Point> bytes;
    private final int width;
    private final int length;

    public DayEighteen(BufferedReader input) throws IOException {
        this(input, 71, 71);
    }

    public DayEighteen(BufferedReader input, int width, int length) throws IOException {
        try (input) {
            bytes = input.lines()
                    .map(it -> it.split(","))
                    .map(it -> new Point(Integer.parseInt(it[0]), Integer.parseInt(it[1])))
                    .toList();
        }
        this.width = width;
        this.length = length;
    }

    @Override
    public String partOneName() {
        return "minimum steps to escape after 1kB";
    }

    @Override
    public Integer partOne() {
        var corruptionGrid = initCorruptionGrid();
        dropBytes(corruptionGrid, getBytesFalling());
        var moveGrid = initMoveGrid();
        traverseGrid(moveGrid, corruptionGrid);
        return moveGrid[length-1][width-1];
    }

    @Override
    public String partTwoName() {
        return "first byte preventing escape";
    }

    @Override
    public String partTwo() {
        var corruptionGrid = initCorruptionGrid();
        for (int i = 0; i < bytes.size(); i++) {
            dropByte(corruptionGrid, i);
            var moveGrid = initMoveGrid();
            traverseGrid(moveGrid, corruptionGrid);
            if (moveGrid[length-1][width-1] == Integer.MAX_VALUE) {
                var obstructingByte = bytes.get(i);
                return obstructingByte.x() + "," + obstructingByte.y();
            }
        }
        throw new IllegalStateException("escape still possible after all bytes have fallen");
    }

    private int getBytesFalling() {
        if (this.width == 7 && this.length == 7) {
            return 12;
        } else if (this.width == 71 && this.length == 71) {
            return 1024;
        } else {
            throw new IllegalStateException("unknown grid size");
        }
    }

    private int[][] initMoveGrid() {
        var moveGrid = new int[length][width];
        for (int row = 0; row < length; row++) {
            Arrays.fill(moveGrid[row], Integer.MAX_VALUE);
        }
        moveGrid[0][0] = 0;
        return moveGrid;
    }

    private boolean[][] initCorruptionGrid() {
        return new boolean[length][width];
    }

    private void dropBytes(boolean[][] corruptionGrid, int numBytes) {
        for (int i = 0; i < numBytes; i++) {
            dropByte(corruptionGrid, i);
        }
    }

    private void dropByte(boolean[][] corruptionGrid, int i) {
        var byteToDrop = bytes.get(i);
        corruptionGrid[byteToDrop.y()][byteToDrop.x()] = true;
    }

    private void traverseGrid(int[][] moveGrid, boolean[][] corruptionGrid) {
        var pathEnds = Set.of(new Point(0, 0));
        while (!pathEnds.isEmpty()) {
            var newPathEnds = new HashSet<Point>();
            for (var pathEnd : pathEnds) {
                int curX = pathEnd.x();
                int curY = pathEnd.y();
                var surrounding = List.of(
                        pathEnd.above(),
                        pathEnd.below(),
                        pathEnd.left(),
                        pathEnd.right());
                for (var newPathEnd : surrounding) {
                    int newX = newPathEnd.x();
                    int newY = newPathEnd.y();
                    if (newX >= 0 && newX < width && newY >= 0 && newY < length &&
                            !corruptionGrid[newY][newX] &&
                            moveGrid[newY][newX] > (moveGrid[curY][curX] + 1)) {
                        moveGrid[newY][newX] = moveGrid[curY][curX] + 1;
                        newPathEnds.add(newPathEnd);
                    }
                }
            }
            pathEnds = newPathEnds;
        }
    }

    public String gridToStr(boolean[][] grid) {
        var buf = new StringBuilder();
        for (int row = 0; row < grid.length; row++) {
            buf.append("\n");
            for (int col = 0; col < grid[0].length; col++) {
                buf.append(grid[row][col] ? '#' : '.');
            }
        }
        return buf.toString();
    }

}
