package day03;

import common.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DayThree implements Day {

    private static final String MUL_PATTERN = "mul\\(([1-9][0-9]*),([1-9][0-9]*)\\)";
    private final String memory;

    public DayThree(BufferedReader input) throws IOException {
        try (input) {
            memory = input.lines()
                    .collect(Collectors.joining());
        }
    }

    @Override
    public String partOneName() {
        return "multiplication sum";
    }

    @Override
    public int partOne() {
        var pattern = Pattern.compile(MUL_PATTERN);
        var matcher = pattern.matcher(memory);
        return matcher.results()
                .map(it -> Integer.parseInt(it.group(1)) * Integer.parseInt(it.group(2)))
                .reduce(0, Integer::sum);
    }

    @Override
    public String partTwoName() {
        return "enabled multiplication sum";
    }

    @Override
    public int partTwo() {
        var pattern = Pattern.compile("(do\\(\\))|(don't\\(\\))|" + MUL_PATTERN);
        var matcher = pattern.matcher(memory);
        var enabled = true;
        var sum = 0;
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                enabled = true;
            } else if (matcher.group(2) != null) {
                enabled = false;
            } else if (enabled) {
                sum += Integer.parseInt(matcher.group(3)) * Integer.parseInt(matcher.group(4));
            }
        }
        return sum;
    }
}
