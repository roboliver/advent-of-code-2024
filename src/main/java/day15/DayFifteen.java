package day15;

import config.Day;
import common.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DayFifteen implements Day<Integer> {

    private final List<String> warehouseLines;
    private final List<Move> moves;

    public DayFifteen(BufferedReader input) throws IOException {
        var warehouseLines = new ArrayList<String>();
        var moveLines = new ArrayList<String>();
        try (input) {
            String line;
            while (!(line = input.readLine()).isEmpty()) {
                warehouseLines.add(line);
            }
            while ((line = input.readLine()) != null) {
                moveLines.add(line);
            }
        }
        this.warehouseLines = warehouseLines;
        this.moves = parseMoves(moveLines);
    }

    private List<Move> parseMoves(List<String> moveLines) {
        return moveLines.stream()
                .flatMapToInt(String::chars)
                .mapToObj(it -> Move.parse((char) it))
                .toList();
    }

    @Override
    public String partOneName() {
        return "box coordinates sum";
    }

    @Override
    public Integer partOne() {
        return doPart(parseWarehouse(this.warehouseLines));
    }

    @Override
    public String partTwoName() {
        return "box coordinates sum (big warehouse)";
    }

    @Override
    public Integer partTwo() {
        return doPart(parseWarehouseWide(this.warehouseLines));
    }

    private int doPart(Tile[][] warehouse) {
        var robotLoc = findRobot(warehouse);
        for (var move : moves) {
            robotLoc = doMove(warehouse, robotLoc, move);
        }
        return boxCoordinateSum(warehouse);
    }

    private Tile[][] parseWarehouse(List<String> warehouseLines) {
        var warehouse = new Tile[warehouseLines.size()][warehouseLines.get(0).length()];
        for (int row = 0; row < warehouseLines.size(); row++) {
            var line = warehouseLines.get(row);
            for (int col = 0; col < line.length(); col++) {
                warehouse[row][col] = Tile.parse(line.charAt(col));
            }
        }
        return warehouse;
    }

    private Tile[][] parseWarehouseWide(List<String> warehouseLines) {
        var warehouse = new Tile[warehouseLines.size()][warehouseLines.get(0).length() * 2];
        for (int row = 0; row < warehouseLines.size(); row++) {
            var line = warehouseLines.get(row);
            for (int col = 0; col < line.length(); col++) {
                var tiles = Tile.parseWide(line.charAt(col));
                warehouse[row][col*2] = tiles.get(0);
                warehouse[row][col*2 + 1] = tiles.get(1);
            }
        }
        return warehouse;
    }

    private Point findRobot(Tile[][] warehouse) {
        for (int row = 0; row < warehouse.length; row++) {
            for (int col = 0; col < warehouse[0].length; col++) {
                if (warehouse[row][col] == Tile.ROBOT) {
                    return new Point(col, row);
                }
            }
        }
        throw new IllegalStateException("no robot found");
    }

    private Point doMove(Tile[][] warehouse, Point robotLoc, Move move) {
        var nextLoc = increment(robotLoc, move);
        var nextTile = tileAt(warehouse, nextLoc);
        return switch (nextTile) {
            case EMPTY -> doMoveEmpty(warehouse, robotLoc, move);
            case BOX -> doMoveBox(warehouse, robotLoc, move);
            case LEFT_BOX, RIGHT_BOX -> doMoveWideBox(warehouse, robotLoc, move);
            default -> robotLoc;
        };
    }

    private Point doMoveEmpty(Tile[][] warehouse, Point robotLoc, Move move) {
        var robotLocNew = increment(robotLoc, move);
        setTile(warehouse, robotLocNew, Tile.ROBOT);
        setTile(warehouse, robotLoc, Tile.EMPTY);
        return robotLocNew;
    }

    private Point doMoveBox(Tile[][] warehouse, Point robotLoc, Move move) {
        var robotLocNew = increment(robotLoc, move);
        var boxLoc = robotLocNew;
        while (tileAt(warehouse, boxLoc) == Tile.BOX) {
            boxLoc = increment(boxLoc, move);
        }
        if (tileAt(warehouse, boxLoc) == Tile.EMPTY) {
            while (!boxLoc.equals(robotLocNew)) {
                setTile(warehouse, boxLoc, Tile.BOX);
                boxLoc = increment(boxLoc, Move.reverse(move));
            }
            setTile(warehouse, robotLocNew, Tile.ROBOT);
            setTile(warehouse, robotLoc, Tile.EMPTY);
            return robotLocNew;
        }
        return robotLoc;
    }

    private Point doMoveWideBox(Tile[][] warehouse, Point robotLoc, Move move) {
        return switch (move) {
            case LEFT, RIGHT -> doMoveWideBoxHorizontal(warehouse, robotLoc, move);
            case UP, DOWN -> doMoveWideBoxVertical(warehouse, robotLoc, move);
        };
    }

    private Point doMoveWideBoxHorizontal(Tile[][] warehouse, Point robotLoc, Move move) {
        var robotLocNew = increment(robotLoc, move);
        Point boxLoc = robotLocNew;
        while (tileAt(warehouse, boxLoc) == Tile.LEFT_BOX || tileAt(warehouse, boxLoc) == Tile.RIGHT_BOX) {
            boxLoc = increment(boxLoc, move);
        }
        if (tileAt(warehouse, boxLoc) == Tile.EMPTY) {
            while (!boxLoc.equals(robotLocNew)) {
                var prevBoxLoc = increment(boxLoc, Move.reverse(move));
                setTile(warehouse, boxLoc, tileAt(warehouse, prevBoxLoc));
                boxLoc = prevBoxLoc;
            }
            setTile(warehouse, robotLocNew, Tile.ROBOT);
            setTile(warehouse, robotLoc, Tile.EMPTY);
            return robotLocNew;
        }
        return robotLoc;
    }

    private Point doMoveWideBoxVertical(Tile[][] warehouse, Point robotLoc, Move move) {
        var robotLocNew = increment(robotLoc, move);
        Point boxLeft;
        Point boxRight;
        if (tileAt(warehouse, robotLocNew) == Tile.LEFT_BOX) {
            boxLeft = robotLocNew;
            boxRight = increment(robotLocNew, Move.RIGHT);
        } else {
            boxLeft = increment(robotLocNew, Move.LEFT);
            boxRight = robotLocNew;
        }
        if (canMoveWideVertical(warehouse, boxLeft, boxRight, move)) {
            execMoveWideVertical(warehouse, boxLeft, boxRight, move);
            setTile(warehouse, robotLocNew, Tile.ROBOT);
            setTile(warehouse, robotLoc, Tile.EMPTY);
            return robotLocNew;
        }
        return robotLoc;
    }

    private boolean canMoveWideVertical(Tile[][] warehouse, Point boxLeft, Point boxRight, Move move) {
        var nextLocLeft = increment(boxLeft, move);
        var nextTileLeft = tileAt(warehouse, nextLocLeft);
        var nextLocRight = increment(boxRight, move);
        var nextTileRight = tileAt(warehouse, nextLocRight);
        if (nextTileLeft == Tile.EMPTY && nextTileRight == Tile.EMPTY) {
            return true;
        } else if (nextTileLeft == Tile.WALL || nextTileRight == Tile.WALL) {
            return false;
        } else if (nextTileLeft == Tile.LEFT_BOX && nextTileRight == Tile.RIGHT_BOX) {
            return canMoveWideVertical(warehouse, nextLocLeft, nextLocRight, move);
        } else {
            boolean canMoveLeft = (nextTileLeft == Tile.EMPTY) ||
                    canMoveWideVertical(warehouse, increment(nextLocLeft, Move.LEFT), nextLocLeft, move);
            boolean canMoveRight = (nextTileRight == Tile.EMPTY) ||
                    canMoveWideVertical(warehouse, nextLocRight, increment(nextLocRight, Move.RIGHT), move);
            return canMoveLeft && canMoveRight;
        }
    }

    private void execMoveWideVertical(Tile[][] warehouse, Point boxLeft, Point boxRight, Move move) {
        var nextLocLeft = increment(boxLeft, move);
        var nextTileLeft = tileAt(warehouse, nextLocLeft);
        var nextLocRight = increment(boxRight, move);
        var nextTileRight = tileAt(warehouse, nextLocRight);
        if (nextTileLeft == Tile.LEFT_BOX && nextTileRight == Tile.RIGHT_BOX) {
            execMoveWideVertical(warehouse, nextLocLeft, nextLocRight, move);
        }
        if (nextTileLeft == Tile.RIGHT_BOX) {
            execMoveWideVertical(warehouse, increment(nextLocLeft, Move.LEFT), nextLocLeft, move);
        }
        if (nextTileRight == Tile.LEFT_BOX) {
            execMoveWideVertical(warehouse, nextLocRight, increment(nextLocRight, Move.RIGHT), move);
        }
        setTile(warehouse, nextLocLeft, Tile.LEFT_BOX);
        setTile(warehouse, nextLocRight, Tile.RIGHT_BOX);
        setTile(warehouse, boxLeft, Tile.EMPTY);
        setTile(warehouse, boxRight, Tile.EMPTY);
    }

    private Point increment(Point loc, Move move) {
        return switch (move) {
            case UP -> new Point(loc.x(), loc.y() - 1);
            case DOWN -> new Point(loc.x(), loc.y() + 1);
            case LEFT -> new Point(loc.x() - 1, loc.y());
            case RIGHT -> new Point(loc.x() + 1, loc.y());
        };
    }

    private Tile tileAt(Tile[][] warehouse, Point loc) {
        return warehouse[loc.y()][loc.x()];
    }

    private void setTile(Tile[][] warehouse, Point loc, Tile newTile) {
        warehouse[loc.y()][loc.x()] = newTile;
    }

    private int boxCoordinateSum(Tile[][] warehouse) {
        int sum = 0;
        for (int row = 0; row < warehouse.length; row++) {
            for (int col = 0; col < warehouse[0].length; col++) {
                if (warehouse[row][col] == Tile.BOX || warehouse[row][col] == Tile.LEFT_BOX) {
                    sum += (100 * row) + col;
                }
            }
        }
        return sum;
    }

    private String warehouseStr(Tile[][] warehouse) {
        var buf = new StringBuilder();
        for (int row = 0; row < warehouse.length; row++) {
            for (int col = 0; col < warehouse[0].length; col++) {
                buf.append(warehouse[row][col]);
            }
            buf.append("\n");
        }
        return buf.toString();
    }
}
