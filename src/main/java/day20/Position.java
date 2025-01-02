package day20;

public record Position(int row, int col) {

    public Position above() {
        return new Position(row - 1, col);
    }

    public Position below() {
        return new Position(row + 1, col);
    }

    public Position left() {
        return new Position(row, col - 1);
    }

    public Position right() {
        return new Position(row, col + 1);
    }
}
