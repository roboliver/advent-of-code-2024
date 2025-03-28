package day05;

import config.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class DayFive implements Day<Integer> {
    // key = page number, value = numbers of pages that should precede key page
    private final Map<Integer, Set<Integer>> pageOrderingRules;
    private final List<List<Integer>> updates;

    public DayFive(BufferedReader input) throws IOException {
        var pageOrderingRules = new HashMap<Integer, Set<Integer>>();
        try (input) {
            String line;
            while (!(line = input.readLine()).isEmpty()) {
                var rule = line.split("\\|");
                var first = Integer.parseInt(rule[0]);
                var second = Integer.parseInt(rule[1]);
                var precedingPages = pageOrderingRules.computeIfAbsent(second, HashSet::new);
                precedingPages.add(first);
            }
            this.pageOrderingRules = Collections.unmodifiableMap(pageOrderingRules);
            this.updates = input.lines()
                    .map(it -> Arrays.stream(it.split(","))
                            .map(Integer::parseInt)
                            .toList())
                    .toList();
        }
    }

    @Override
    public String partOneName() {
        return "middle page sum of correctly-ordered updates";
    }

    @Override
    public Integer partOne() {
        return updates.stream()
                .filter(this::isOrdered)
                .map(this::getMiddlePage)
                .reduce(0, Integer::sum);
    }

    @Override
    public String partTwoName() {
        return "middle page sum of incorrectly-ordered updates, after ordering";
    }

    @Override
    public Integer partTwo() {
        return updates.stream()
                .filter(update -> !isOrdered(update))
                .map(this::orderPages)
                .map(this::getMiddlePage)
                .reduce(0, Integer::sum);
    }

    private List<Integer> orderPages(List<Integer> update) {
        var updateOrdered = new ArrayList<>(update);
        updateOrdered.sort(pageComparator());
        return updateOrdered;
    }

    private Comparator<Integer> pageComparator() {
        return (page1, page2) -> {
            if (pageOrderingRules.getOrDefault(page2, Set.of()).contains(page1)) {
                return -1;
            } else if (pageOrderingRules.getOrDefault(page1, Set.of()).contains(page2)) {
                return 1;
            } else {
                return 0;
            }
        };
    }

    private boolean isOrdered(List<Integer> update) {
        var expectedAlready = new HashSet<>();
        for (var page : update) {
            if (expectedAlready.contains(page)) {
                return false;
            }
            expectedAlready.addAll(pageOrderingRules.getOrDefault(page, Set.of()));
        }
        return true;
    }

    private int getMiddlePage(List<Integer> update) {
        return update.get(update.size() / 2);
    }
}
