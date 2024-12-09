package day01;

import common.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DayOne implements Day {

    private final List<Integer> leftNums;
    private final List<Integer> rightNums;

    public DayOne(BufferedReader lines) throws IOException {
        var leftNums = new ArrayList<Integer>();
        var rightNums = new ArrayList<Integer>();
        try (lines) {
            String line;
            while ((line = lines.readLine()) != null) {
                var numPair = line.split(" {3}");
                leftNums.add(Integer.parseInt(numPair[0]));
                rightNums.add(Integer.parseInt(numPair[1]));
            }
        }
        this.leftNums = Collections.unmodifiableList(leftNums);
        this.rightNums = Collections.unmodifiableList(rightNums);
    }

    @Override
    public String partOneName() {
        return "total distance";
    }

    @Override
    public int partOne() {
        var leftNumsCopy = new ArrayList<>(this.leftNums);
        var rightNumsCopy = new ArrayList<>(this.rightNums);
        Collections.sort(leftNumsCopy);
        Collections.sort(rightNumsCopy);
        return IntStream.range(0, leftNumsCopy.size())
                .map(it -> Math.abs(leftNumsCopy.get(it) - rightNumsCopy.get(it)))
                .sum();
    }

    @Override
    public String partTwoName() {
        return "similarity score";
    }

    @Override
    public int partTwo() {
        var rightNumCounts = rightNums.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()));
        return (int) leftNums.stream()
                .map(it -> it * rightNumCounts.getOrDefault(it, 0L))
                .mapToLong(Long::longValue)
                .sum();
    }
}
