package day06;

import common.Day;
import common.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class DaySix implements Day {

    private final Set<Point> obstacles;
    private final DirectedPosition startDirPos;
    private final int width;
    private final int length;

    public DaySix(BufferedReader input) throws IOException {
        var obstacles = new HashSet<Point>();
        String line;
        int row = 0;
        Point startPos = null;
        Direction startDir = null;
        int width = 0;
        while ((line = input.readLine()) != null) {
            var chars = line.toCharArray();
            for (int col = 0; col < chars.length; col++) {
                var tile = chars[col];
                if (tile == '#') {
                    obstacles.add(new Point(col, row));
                } else if (Direction.isGuard(tile)) {
                    startPos = new Point(col, row);
                    startDir = Direction.fromChar(tile);
                }
            }
            if (row == 0) {
                width = chars.length;
            }
            row++;
        }
        this.obstacles = Collections.unmodifiableSet(obstacles);
        this.startDirPos = new DirectedPosition(startPos, startDir);
        this.length = row;
        this.width = width;
    }

    @Override
    public String partOneName() {
        return "distinct positions visited by guard";
    }

    @Override
    public int partOne() {
        var visited = new HashSet<Point>();
        var currentDirPos = startDirPos;
        do {
            visited.add(currentDirPos.pos());
            currentDirPos = nextDirPos(currentDirPos, obstacles);
        } while (inMap(currentDirPos.pos()));
        return visited.size();
    }

    @Override
    public String partTwoName() {
        return "possible positions for obstruction";
    }

    @Override
    public int partTwo() {
        var defaultRoute = calculateRoute();
        var routeTraversed = new HashSet<DirectedPosition>();
        var tilesVisited = new HashSet<Point>();
        var infiniteLoopPositions = new HashSet<Point>();
        for (int i = 1; i < defaultRoute.size(); i++) {
            var currentDirPos = defaultRoute.get(i - 1);
            routeTraversed.add(currentDirPos);
            tilesVisited.add(currentDirPos.pos());
            var divertedRouteTraversed = new HashSet<DirectedPosition>();
            var newObstacle = currentDirPos.dir().move(currentDirPos.pos());
            if (obstacles.contains(newObstacle) || tilesVisited.contains(newObstacle)) {
                continue;
            }
            var obstaclesWithNew = new HashSet<>(this.obstacles);
            obstaclesWithNew.add(newObstacle);
            do {
                currentDirPos = nextDirPos(currentDirPos, obstaclesWithNew);
                if (routeTraversed.contains(currentDirPos) || divertedRouteTraversed.contains(currentDirPos)) {
                    infiniteLoopPositions.add(newObstacle);
                    break;
                }
                divertedRouteTraversed.add(currentDirPos);
            } while (inMap(currentDirPos.pos()));
        }
        return infiniteLoopPositions.size();
    }

    private List<DirectedPosition> calculateRoute() {
        var route = new ArrayList<DirectedPosition>();
        var currentDirPos = startDirPos;
        do {
            route.add(currentDirPos);
            currentDirPos = nextDirPos(currentDirPos, obstacles);
        } while (inMap(currentDirPos.pos()));
        return route;
    }

    private DirectedPosition nextDirPos(DirectedPosition currentDirPos, Set<Point> obstacles) {
        return nextDirPos(currentDirPos, obstacles, null);
    }

    private DirectedPosition nextDirPos(DirectedPosition currentDirPos, Set<Point> obstacles, Point extraObstacle) {
        var currentPos = currentDirPos.pos();
        var currentDir = currentDirPos.dir();
        var nextPos = currentDir.move(currentPos);
        if (nextPos == extraObstacle || obstacles.contains(nextPos)) {
            currentDir = Direction.rotate(currentDir);
        } else {
            currentPos = nextPos;
        }
        return new DirectedPosition(currentPos, currentDir);
    }

    private boolean inMap(Point pos) {
        return pos.x() >= 0 && pos.x() < this.width
                && pos.y() >= 0 && pos.y() < this.length;
    }
}
