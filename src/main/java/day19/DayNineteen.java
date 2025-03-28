package day19;

import config.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;

public class DayNineteen implements Day<Long> {

    private final Set<String> towels;
    private final List<String> designs;

    public DayNineteen(BufferedReader input) throws IOException {
        try (input) {
            var towelLine = input.readLine();
            this.towels = Set.of(towelLine.split(", "));
            input.readLine();
            designs = input.lines()
                    .toList();
        }
    }

    @Override
    public String partOneName() {
        return "possible designs";
    }

    @Override
    public Long partOne() {
        int longestTowel = longestTowel();
        return designs.stream()
                .filter(it -> waysToMakeDesign(it, longestTowel) > 0)
                .count();
    }

    @Override
    public String partTwoName() {
        return "possible ways to make all designs";
    }

    @Override
    public Long partTwo() {
        int longestTowel = longestTowel();
        return designs.stream()
                .mapToLong(it -> waysToMakeDesign(it, longestTowel))
                .sum();
    }

    private long waysToMakeDesign(String design, int longestTowel) {
        var arrangements = new ArrayDeque<ArrangementCount>();
        arrangements.add(new ArrangementCount("", 1L));
        for (int i = 1; i <= design.length(); i++) {
            var newArrangement = design.substring(0, i);
            long newArrangementCount = arrangements.stream()
                    .filter(it -> {
                        var towelNeeded = design.substring(it.arrangement().length(), newArrangement.length());
                        return towels.contains(towelNeeded);
                    })
                    .mapToLong(ArrangementCount::count)
                    .sum();
            if (arrangements.size() == longestTowel) {
                arrangements.removeFirst();
            }
            arrangements.addLast(new ArrangementCount(newArrangement, newArrangementCount));
        }
        return arrangements.getLast().count();
    }

    private int longestTowel() {
        return towels.stream()
                .mapToInt(String::length)
                .max()
                .orElseThrow();
    }

}
