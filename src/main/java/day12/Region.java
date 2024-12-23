package day12;

import java.util.*;

public class Region {

    private final List<Plant> plants = new ArrayList<>();

    public Plant addPlant(char type, int row, int col) {
        var plant = new Plant(type, row, col, this);
        plants.add(plant);
        return plant;
    }

    public int area() {
        return plants.size();
    }

    public int perimeter() {
        return plants.stream()
                .map(Plant::borderSides)
                .mapToInt(it -> it)
                .sum();
    }

    public int numberOfSides() {
        int sides = 0;
        for (var direction : Direction.values()) {
            var bordersInDir = new HashMap<Integer, Set<Integer>>();
            for (var plant : plants) {
                if (!plant.hasAdjacent(direction)) {
                    boolean isHorizontal = (direction == Direction.UP) || (direction == Direction.DOWN);
                    int key = isHorizontal ? plant.getRow() : plant.getCol();
                    int value = isHorizontal ? plant.getCol() : plant.getRow();
                    bordersInDir.computeIfAbsent(key, HashSet::new)
                            .add(value);
                }
            }
            for (var alignedBorders : bordersInDir.values()) {
                for (int border : alignedBorders) {
                    if (!alignedBorders.contains(border - 1)) {
                        sides++;
                    }
                }
            }
        }
        return sides;
    }

    @Override
    public String toString() {
        return plants.toString();
    }
}
