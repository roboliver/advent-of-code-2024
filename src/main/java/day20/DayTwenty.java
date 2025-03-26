package day20;

import common.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class DayTwenty implements Day<Integer> {

    private Position start;
    private Position end;
    private final boolean[][] obstacles;
    private final int threshold;

    public DayTwenty(BufferedReader input) throws IOException {
        this(input, 100);
    }

    public DayTwenty(BufferedReader input, int threshold) throws IOException {
        List<String> lines;
        try (input) {
            lines = input.lines()
                    .toList();
        }
        this.obstacles = new boolean[lines.size()][lines.get(0).length()];
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char tile = line.charAt(col);
                if (tile == 'S') {
                    this.start = new Position(row, col);
                } else if (tile == 'E') {
                    this.end = new Position(row, col);
                } else if (tile == '#') {
                    obstacles[row][col] = true;
                }
            }
        }
        this.threshold = threshold;
    }

    @Override
    public String partOneName() {
        return "cheats saving over 100ps (2ps cheat duration)";
    }

    @Override
    public Integer partOne() {
        return cheatsOverThreshold(2);
    }

    @Override
    public String partTwoName() {
        return "cheats saving over 100ps (20ps cheat duration)";
    }

    @Override
    public Integer partTwo() {
        return cheatsOverThreshold(20);
    }

    private int cheatsOverThreshold(int cheatDuration) {
        int cheatsOverThreshold = 0;
        var moves = initMoves();
        for (int row = 0; row < moves.length; row++) {
            for (int col = 0; col < moves[0].length; col++) {
                if (obstacles[row][col]) {
                    continue;
                }
                var to = new Position(row, col);
                for (var from : getCheatableFrom(to, cheatDuration)) {
                    int fromRow = from.row();
                    int fromCol = from.col();
                    if (fromRow >= 0 && fromRow < moves.length &&
                            fromCol >= 0 && fromCol < moves[0].length &&
                            !obstacles[fromRow][fromCol] &&
                            moves[row][col] - moves[fromRow][fromCol] - distance(from, to) >= threshold) {
                        cheatsOverThreshold++;
                    }
                }
            }
        }
        return cheatsOverThreshold;
    }

    private int[][] initMoves() {
        int[][] moves = new int[obstacles.length][obstacles[0].length];
        for (int[] row : moves) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        moves[start.row()][start.col()] = 0;
        traverse(moves);
        return moves;
    }

    private void traverse(int[][] moves) {
        var pathEnds = List.of(start);
        while (!pathEnds.isEmpty()) {
            var newPathEnds = new ArrayList<Position>();
            for (var pathEnd : pathEnds) {
                int curRow = pathEnd.row();
                int curCol = pathEnd.col();
                var surrounding = List.of(
                        pathEnd.above(),
                        pathEnd.below(),
                        pathEnd.left(),
                        pathEnd.right());
                for (var newPathEnd : surrounding) {
                    int newRow = newPathEnd.row();
                    int newCol = newPathEnd.col();
                    if (newRow >= 0 && newRow < moves.length &&
                            newCol >= 0 && newCol < moves[0].length &&
                            !obstacles[newRow][newCol] &&
                            moves[newRow][newCol] > moves[curRow][curCol] + 1) {
                        moves[newRow][newCol] = moves[curRow][curCol] + 1;
                        newPathEnds.add(newPathEnd);
                    }
                }
            }
            pathEnds = newPathEnds;
        }
    }

    private List<Position> getCheatableFrom(Position to, int cheatDuration) {
        var from = new ArrayList<Position>();
        var outerRing = new ArrayList<Position>();
        outerRing.add(to);
        for (int i = 0; i < cheatDuration; i++) {
            var newOuterRing = new ArrayList<Position>();
            for (var position : outerRing) {
                if (position.row() <= to.row() && position.col() >= to.col()) {
                    newOuterRing.add(position.above());
                }
                if (position.row() >= to.row() && position.col() <= to.col()) {
                    newOuterRing.add(position.below());
                }
                if (position.row() <= to.row() && position.col() <= to.col()) {
                    newOuterRing.add(position.left());
                }
                if (position.row() >= to.row() && position.col() >= to.col()) {
                    newOuterRing.add(position.right());
                }
            }
            outerRing = newOuterRing;
            from.addAll(outerRing);
        }
        return from;
    }

    private int distance(Position from, Position to) {
        return Math.abs(from.row() - to.row()) + Math.abs(from.col() - to.col());
    }

    private String movesToStr(int[][] moves) {
        var buf = new StringBuilder();
        for (int row = 0; row < moves.length; row++) {
            for (int col = 0; col < moves[0].length; col++) {
                buf.append("[");
                var it = moves[row][col];
                if (new Position(row, col).equals(start)) {
                    buf.append("S_");
                } else if (new Position(row, col).equals(end)) {
                    buf.append("E_");
                } else if (it == Integer.MAX_VALUE) {
                    buf.append("##");
                } else {
                    buf.append(String.format("%2d", moves[row][col]));
                }
                buf.append("]");
            }
            buf.append("\n");
        }
        return buf.toString();
    }

}
