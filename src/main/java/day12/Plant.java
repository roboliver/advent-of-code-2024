package day12;

import java.util.HashMap;
import java.util.Map;

public class Plant {

    private final char type;
    private final int row;
    private final int col;
    private final Region region;
    private final Map<Direction, Plant> adjacents = new HashMap<>();

    public Plant(char type, int row, int col, Region region) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.region = region;
    }

    public void addAdjacent(Plant other, Direction direction) {
        adjacents.put(direction, other);
        other.adjacents.put(Direction.reverse(direction), this);
    }

    public char getType() {
        return this.type;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public Region getRegion() {
        return this.region;
    }

    public int borderSides() {
        return 4 - adjacents.size();
    }

    public boolean hasAdjacent(Direction direction) {
        return adjacents.containsKey(direction);
    }

    @Override
    public String toString() {
        return type + "[" + row + "," + col + "]";
    }
}
