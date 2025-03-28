package day12;

import config.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayTwelve implements Day<Integer> {

    private final List<Region> regions;

    public DayTwelve(BufferedReader input) throws IOException {
        List<char[]> lines;
        try (input) {
            lines = input.lines()
                    .map(String::toCharArray)
                    .toList();
        }
        char[][] plantTypes = new char[lines.size()][];
        Plant[][] plants = new Plant[plantTypes.length][];
        for (int i = 0; i < lines.size(); i++) {
            plantTypes[i] = lines.get(i);
            plants[i] = new Plant[plantTypes[i].length];
        }
        var regions = buildRegions(plantTypes, plants);
        attachAdjacentPlants(plants);
        this.regions = Collections.unmodifiableList(regions);
    }

    private List<Region> buildRegions(char[][] plantTypes, Plant[][] plants) {
        var regions = new ArrayList<Region>();
        for (int row = 0; row < plantTypes.length; row++) {
            for (int col = 0; col < plantTypes[0].length; col++) {
                if (plants[row][col] == null) {
                    regions.add(buildRegion(row, col, plantTypes, plants));
                }
            }
        }
        return regions;
    }

    private Region buildRegion(int row, int col, char[][] plantTypes, Plant[][] plants) {
        char type = plantTypes[row][col];
        var region = new Region();
        var plant = region.addPlant(type, row, col);
        plants[row][col] = plant;
        expandRegion(row, col, plantTypes, plants, region);
        return region;
    }

    private void expandRegion(int row, int col, char[][] plantTypes, Plant[][] plants, Region region) {
        char type = plants[row][col].getType();
        expandToPlant(type, row-1, col, plantTypes, plants, region);
        expandToPlant(type, row+1, col, plantTypes, plants, region);
        expandToPlant(type, row, col-1, plantTypes, plants, region);
        expandToPlant(type, row, col+1, plantTypes, plants, region);
    }

    private void expandToPlant(char type, int row, int col, char[][] plantTypes, Plant[][] plants, Region region) {
        if (row >= 0 && row < plantTypes.length &&
                col >= 0 && col < plantTypes[0].length &&
                plants[row][col] == null &&
                plantTypes[row][col] == type) {
            var newPlant = region.addPlant(type, row, col);
            plants[row][col] = newPlant;
            expandRegion(row, col, plantTypes, plants, region);
        }
    }

    private void attachAdjacentPlants(Plant[][] plants) {
        for (int row = 0; row < plants.length; row++) {
            for (int col = 0; col < plants[0].length; col++) {
                var plant = plants[row][col];
                if (row > 0) {
                    var abovePlant = plants[row-1][col];
                    if (plant.getRegion() == abovePlant.getRegion()) {
                        plant.addAdjacent(abovePlant, Direction.UP);
                    }
                }
                if (col > 0) {
                    var leftPlant = plants[row][col-1];
                    if (plant.getRegion() == leftPlant.getRegion()) {
                        plant.addAdjacent(leftPlant, Direction.LEFT);
                    }
                }
            }
        }
    }

    @Override
    public String partOneName() {
        return "total fencing price";
    }

    @Override
    public Integer partOne() {
        return regions.stream()
                .map(it -> it.area() * it.perimeter())
                .mapToInt(it -> it)
                .sum();
    }

    @Override
    public String partTwoName() {
        return "total fencing price with bulk discount";
    }

    @Override
    public Integer partTwo() {
        return regions.stream()
                .map(it -> it.area() * it.numberOfSides())
                .mapToInt(it -> it)
                .sum();
    }
}
