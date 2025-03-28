package day11;

import config.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayEleven implements Day<Long> {

    private final Map<Long, Long> stoneCounts;

    public DayEleven(BufferedReader input) throws IOException {
        List<Long> stones;
        try (input) {
            stones = Arrays.stream(input.readLine().split(" "))
                    .map(Long::parseLong)
                    .toList();
        }
        stoneCounts = new HashMap<>();
        for (var stone : stones) {
            stoneCounts.put(stone, 1L);
        }
    }

    @Override
    public String partOneName() {
        return "stones after 25 blinks";
    }

    @Override
    public Long partOne() {
        return calculateStones(25);
    }

    @Override
    public String partTwoName() {
        return "stones after 75 blinks";
    }

    @Override
    public Long partTwo() {
        return calculateStones(75);
    }

    private long calculateStones(int blinks) {
        var stoneCounts = new HashMap<>(this.stoneCounts);
        for (int i = 0; i < blinks; i++) {
            var newStoneCounts = new HashMap<Long, Long>();
            for (Map.Entry<Long, Long> entry : stoneCounts.entrySet()) {
                long stone = entry.getKey();
                long count = entry.getValue();
                if (stone == 0L) {
                    newStoneCounts.merge(1L, count, Long::sum);
                } else if (Long.toString(stone).length() % 2 == 0) {
                    var stoneStr = Long.toString(stone);
                    var leftNewStoneStr = stoneStr.substring(0, stoneStr.length() / 2);
                    var rightNewStoneStr = stoneStr.substring(stoneStr.length() / 2);
                    newStoneCounts.merge(Long.parseLong(leftNewStoneStr), count, Long::sum);
                    newStoneCounts.merge(Long.parseLong(rightNewStoneStr), count, Long::sum);
                } else {
                    newStoneCounts.merge(stone * 2024L, count, Long::sum);
                }
            }
            stoneCounts.clear();
            stoneCounts.putAll(newStoneCounts);
        }
        return stoneCounts.values().stream()
                .reduce(0L, Long::sum);
    }
}
