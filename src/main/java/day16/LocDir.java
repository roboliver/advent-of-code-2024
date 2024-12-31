package day16;

public record LocDir(Location loc, Direction dir) {

    public LocDir turnClockwise() {
        var newDir = switch (dir) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
        return new LocDir(loc, newDir);
    }

    public LocDir turnCounterClockwise() {
        var newDir = switch (dir) {
            case NORTH -> Direction.WEST;
            case EAST -> Direction.NORTH;
            case SOUTH -> Direction.EAST;
            case WEST -> Direction.SOUTH;
        };
        return new LocDir(loc, newDir);
    }

    public LocDir moveForward() {
        var newLoc = switch (dir) {
            case NORTH -> new Location(loc.row() - 1, loc.col());
            case EAST -> new Location(loc.row(), loc.col() + 1);
            case SOUTH -> new Location(loc.row() + 1, loc.col());
            case WEST -> new Location(loc.row(), loc.col() - 1);
        };
        return new LocDir(newLoc, dir);
    }

    public LocDir moveBackward() {
        var newLoc = switch (dir) {
            case NORTH -> new Location(loc.row() + 1, loc.col());
            case EAST -> new Location(loc.row(), loc.col() - 1);
            case SOUTH -> new Location(loc.row() - 1, loc.col());
            case WEST -> new Location(loc.row(), loc.col() + 1);
        };
        return new LocDir(newLoc, dir);
    }
}
