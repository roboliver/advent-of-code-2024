package day16;

import java.util.Map;
import java.util.stream.IntStream;

public class Tile {

    private final TileType type;
    private int northScore = Integer.MAX_VALUE;
    private int eastScore = Integer.MAX_VALUE;
    private int southScore = Integer.MAX_VALUE;
    private int westScore = Integer.MAX_VALUE;

    public Tile(TileType type) {
        this.type = type;
        if (type == TileType.START) {
            eastScore = 0;
        }
    }

    public TileType type() {
        return this.type;
    }

    public boolean isWall() {
        return this.type == TileType.WALL;
    }

    public int getScore(Direction direction) {
        validateNotWall();
        return switch (direction) {
            case NORTH -> northScore;
            case EAST -> eastScore;
            case SOUTH -> southScore;
            case WEST -> westScore;
        };
    }

    public void setScore(int score, Direction direction) {
        validateNotWall();
        switch (direction) {
            case NORTH -> northScore = score;
            case EAST -> eastScore = score;
            case SOUTH -> southScore = score;
            case WEST -> westScore = score;
        }
    }

    public int getMinScore() {
        validateNotWall();
        return IntStream.of(northScore, eastScore, southScore, westScore)
                .min()
                .orElseThrow();
    }

    public Direction getMinScoreDir() {
        validateNotWall();
        return Map.of(
                Direction.NORTH, northScore,
                Direction.EAST, eastScore,
                Direction.SOUTH, southScore,
                Direction.WEST, westScore)
                .entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
    }

    private void validateNotWall() {
        if (isWall()) {
            throw new IllegalStateException("this is a wall");
        }
    }
}
