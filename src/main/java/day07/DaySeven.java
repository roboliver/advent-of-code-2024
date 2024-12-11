package day07;

import common.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

public class DaySeven implements Day<Long> {

    private final List<Equation> equations;

    public DaySeven(BufferedReader input) throws IOException {
        try (input) {
            equations = input.lines()
                    .map(this::parseEquation)
                    .toList();
        }
    }

    private Equation parseEquation(String line) {
        var parts = line.split(": ");
        var test = Long.parseLong(parts[0]);
        var numbers = Arrays.stream(parts[1].split(" "))
                .map(Long::parseLong)
                .toList();
        return new Equation(test, numbers);
    }

    @Override
    public String partOneName() {
        return "total calibration result";
    }

    @Override
    public Long partOne() {
        return equations.stream()
                .filter(it -> hasSolution(it.test(), new ArrayDeque<>(it.numbers()), false))
                .map(Equation::test)
                .reduce(0L, Long::sum);
    }

    @Override
    public String partTwoName() {
        return "total calibration result (including concat)";
    }

    @Override
    public Long partTwo() {
        return equations.stream()
                .filter(it -> hasSolution(it.test(), new ArrayDeque<>(it.numbers()), true))
                .map(Equation::test)
                .reduce(0L, Long::sum);
    }

    private boolean hasSolution(long test, ArrayDeque<Long> numbers, boolean withConcat) {
        long last = numbers.removeLast();
        boolean solutionFound;
        if (numbers.isEmpty()) {
            solutionFound = (test == last);
        } else {
            solutionFound = (withConcat && lastMatches(test, last) && hasSolution(removeLast(test, last), numbers, true)) ||
                    (test % last == 0 && hasSolution(test / last, numbers, withConcat)) ||
                    hasSolution(test - last, numbers, withConcat);
        }
        numbers.addLast(last);
        return solutionFound;
    }

    private boolean lastMatches(long test, long last) {
        return String.valueOf(test).endsWith(String.valueOf(last));
    }

    private long removeLast(long test, long last) {
        return (long) (test / Math.pow(10, String.valueOf(last).length()));
    }
}
