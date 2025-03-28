package day16;

import config.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DaySixteen implements Day<Integer> {

    private final List<String> lines;

    public DaySixteen(BufferedReader input) throws IOException {
        try (input) {
            this.lines = input.lines().toList();
        }
    }

    @Override
    public String partOneName() {
        return "lowest score possible";
    }

    @Override
    public Integer partOne() {
        var maze = parseAndTraverseMaze();
        return getTile(maze, getEnd(maze)).getMinScore();
    }

    @Override
    public String partTwoName() {
        return "tiles on best paths";
    }

    @Override
    public Integer partTwo() {
        var maze = parseAndTraverseMaze();
        return findBestSeats(maze).size();
    }

    private Tile[][] parseAndTraverseMaze() {
        var maze = parseMaze();
        var pathEnds = Set.of(new LocDir(getStart(maze), Direction.EAST));
        while (!pathEnds.isEmpty()) {
            var newPathEnds = new HashSet<LocDir>();
            for (var pathEnd : pathEnds) {
                explorePath(maze, pathEnd, pathEnd.turnClockwise(), 1000, newPathEnds);
                explorePath(maze, pathEnd, pathEnd.turnCounterClockwise(), 1000, newPathEnds);
                explorePath(maze, pathEnd, pathEnd.moveForward(), 1, newPathEnds);
            }
            pathEnds = newPathEnds;
        }
        return maze;
    }

    private Tile[][] parseMaze() {
        var maze = new Tile[lines.size()][lines.get(0).length()];
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char tileChar = line.charAt(col);
                maze[row][col] = new Tile(TileType.parse(tileChar));
            }
        }
        return maze;
    }

    private void explorePath(Tile[][] maze, LocDir pathEnd, LocDir newPathEnd, int scoreIncrease, Set<LocDir> newPathEnds) {
        var curTile = getTile(maze, pathEnd.loc());
        var newTile = getTile(maze, newPathEnd.loc());
        if (!newTile.isWall()) {
            int newScore = curTile.getScore(pathEnd.dir()) + scoreIncrease;
            if (newScore < newTile.getScore(newPathEnd.dir())) {
                newTile.setScore(newScore, newPathEnd.dir());
                newPathEnds.add(newPathEnd);
            }
        }
    }

    private Set<Location> findBestSeats(Tile[][] maze) {
        var bestPathStarts = new HashSet<LocDir>();
        var end = getEnd(maze);
        bestPathStarts.add(new LocDir(end, getTile(maze, end).getMinScoreDir()));
        var bestSeats = new HashSet<Location>();
        while (!bestPathStarts.isEmpty()) {
            var newBestPathStarts = new HashSet<LocDir>();
            for (var bestPathStart : bestPathStarts) {
                bestSeats.add(bestPathStart.loc());
                var prevPathStarts = List.of(
                        bestPathStart.turnClockwise(),
                        bestPathStart.turnCounterClockwise(),
                        bestPathStart.moveBackward());
                for (var prevPathStart : prevPathStarts) {
                    if (getScore(maze, prevPathStart) < getScore(maze, bestPathStart)) {
                        newBestPathStarts.add(prevPathStart);
                    }
                }
            }
            bestPathStarts = newBestPathStarts;
        }
        return bestSeats;
    }

    private Location getStart(Tile[][] maze) {
        return new Location(maze.length - 2, 1);
    }

    private Location getEnd(Tile[][] maze) {
        return new Location(1, maze[0].length - 2);
    }

    private Tile getTile(Tile[][] maze, Location loc) {
        return maze[loc.row()][loc.col()];
    }

    private int getScore(Tile[][] maze, LocDir locDir) {
        var tile = maze[locDir.loc().row()][locDir.loc().col()];
        if (tile.isWall()) {
            return Integer.MAX_VALUE;
        }
        return tile.getScore(locDir.dir());
    }

    private String mazeToStr(Tile[][] maze, Set<Location> bestSeats) {
        var buf = new StringBuilder();
        for (int row = 0; row < maze.length; row++) {
            buf.append("\n");
            for (int col = 0; col < maze[0].length; col++) {
                if (bestSeats.contains(new Location(row, col))) {
                    buf.append('O');
                } else {
                    buf.append(maze[row][col].type());
                }
            }
        }
        return buf.toString();
    }
}
