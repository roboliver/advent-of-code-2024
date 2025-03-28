package day10;

import config.Day;
import common.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DayTen implements Day<Integer> {

    private final int[][] topography;

    public DayTen(BufferedReader input) throws IOException {
        List<int[]> lines;
        try (input) {
            lines = input.lines()
                    .map(it -> it.chars()
                            .map(Character::getNumericValue)
                            .toArray())
                    .toList();
        }
        topography = new int[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            topography[i] = lines.get(i);
        }
    }

    @Override
    public String partOneName() {
        return "trailhead scores sum";
    }

    @Override
    public Integer partOne() {
        int sum = 0;
        for (int row = 0; row < topography.length; row++) {
            for (int col = 0; col < topography.length; col++) {
                if (topography[row][col] == 0) {
                    sum += calculateTrailheadScoreSum(row, col);
                }
            }
        }
        return sum;
    }

    @Override
    public String partTwoName() {
        return "trailhead ratings sum";
    }

    @Override
    public Integer partTwo() {
        int sum = 0;
        for (int row = 0; row < topography.length; row++) {
            for (int col = 0; col < topography.length; col++) {
                if (topography[row][col] == 0) {
                    sum += calculateTrailheadRatingSum(row, col);
                }
            }
        }
        return sum;    }

    private int calculateTrailheadScoreSum(int row, int col) {
        var peaks = new HashSet<Point>();
        calculateTrailheads(row, col, peaks);
        return peaks.size();
    }

    private int calculateTrailheadRatingSum(int row, int col) {
        return calculateTrailheads(row, col, new HashSet<>());
    }

    private int calculateTrailheads(int row, int col, Set<Point> peaks) {
        int height = topography[row][col];
        if (height == 9) {
            peaks.add(new Point(col, row));
            return 1;
        }
        int ratingSum = 0;
        if (row > 0 && topography[row-1][col] == height + 1) {
            ratingSum += calculateTrailheads(row-1, col, peaks);
        }
        if (row < topography.length - 1 && topography[row+1][col] == height + 1) {
            ratingSum += calculateTrailheads(row+1, col, peaks);
        }
        if (col > 0 && topography[row][col-1] == height + 1) {
            ratingSum += calculateTrailheads(row, col-1, peaks);
        }
        if (col < topography[0].length - 1 && topography[row][col+1] == height + 1) {
            ratingSum += calculateTrailheads(row, col+1, peaks);
        }
        return ratingSum;
    }
}
