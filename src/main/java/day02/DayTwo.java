package day02;

import common.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DayTwo implements Day {

    private final List<List<Integer>> reports;

    public DayTwo(BufferedReader lines) throws IOException {
        var reports = new ArrayList<List<Integer>>();
        try (lines) {
            String line;
            while ((line = lines.readLine()) != null) {
                var report = Arrays.stream(line.split(" "))
                        .map(Integer::parseInt)
                        .toList();
                reports.add(report);
            }
        }
        this.reports = Collections.unmodifiableList(reports);
    }

    @Override
    public String partOneName() {
        return "safe reports";
    }

    @Override
    public int partOne() {
        return (int) reports.stream()
                .filter(this::isSafe)
                .count();
    }

    @Override
    public String partTwoName() {
        return "actual safe reports";
    }

    @Override
    public int partTwo() {
        return (int) reports.stream()
                .filter(this::isSafeActual)
                .count();
    }

    private boolean isSafe(List<Integer> report) {
        List<Integer> reportAscending;
        if (report.get(0) < report.get(1)) {
            reportAscending = report;
        } else {
            reportAscending = new ArrayList<>(report);
            Collections.reverse(reportAscending);
        }
        for (int i = 0; i < reportAscending.size() - 1; i++) {
            if (!isLevelPairSafeAscending(reportAscending.get(i), reportAscending.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    private boolean isSafeActual(List<Integer> report) {
        if (isSafeActualAscending(report)) {
            return true;
        } else {
            var reportReversed = new ArrayList<>(report);
            Collections.reverse(reportReversed);
            return isSafeActualAscending(reportReversed);
        }
    }

    private boolean isSafeActualAscending(List<Integer> reportAscending) {
        var badLevelFound = false;
        for (int i = 0; i < reportAscending.size() - 1; i++) {
            if (!isLevelPairSafeAscending(reportAscending.get(i), reportAscending.get(i + 1))) {
                if (badLevelFound) {
                    return false;
                }
                badLevelFound = true;
                var canIgnore = ignoreFirst(i) || ignoreLast(reportAscending, i) ||
                        ignoreCurrent(reportAscending, i) || ignoreNext(reportAscending, i);
                if (!canIgnore) {
                    return false;
                }
                if (ignoreNext(reportAscending, i)) {
                    i++; // skip the bad level
                }
            }
        }
        return true;
    }

    private boolean ignoreLast(List<Integer> reportAscending, int i) {
        return i == reportAscending.size() - 2;
    }

    private boolean ignoreFirst(int i) {
        return i == 0;
    }

    private boolean ignoreNext(List<Integer> reportAscending, int i) {
        return i < reportAscending.size() - 2 &&
                isLevelPairSafeAscending(reportAscending.get(i), reportAscending.get(i + 2));
    }

    private boolean ignoreCurrent(List<Integer> reportAscending, int i) {
        return i > 0 &&
                isLevelPairSafeAscending(reportAscending.get(i - 1), reportAscending.get(i + 1));
    }

    boolean isLevelPairSafeAscending(int first, int second) {
        var diff = second - first;
        return diff >= 1 && diff <= 3;
    }
}
