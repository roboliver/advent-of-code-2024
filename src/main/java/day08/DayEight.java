package day08;

import common.Day;
import common.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class DayEight implements Day<Integer> {

    private final Map<Character, List<Point>> antennas;
    private final int width;
    private final int length;

    public DayEight(BufferedReader input) throws IOException {
        var antennas = new HashMap<Character, List<Point>>();
        try (input) {
            String line;
            int row = 0;
            int width = 0;
            while((line = input.readLine()) != null) {
                var chars = line.toCharArray();
                if (row == 0) {
                    width = chars.length;
                }
                for (int col = 0; col < chars.length; col++) {
                    if (chars[col] != '.') {
                        var points = antennas.computeIfAbsent(chars[col], ArrayList::new);
                        points.add(new Point(col, row));
                    }
                }
                row++;
            }
            this.antennas = Collections.unmodifiableMap(antennas);
            this.width = width;
            this.length = row;
        }
    }

    @Override
    public String partOneName() {
        return "antinode locations";
    }

    @Override
    public Integer partOne() {
        return calculateAntinodes(true, it -> false);
    }

    @Override
    public String partTwoName() {
        return "actual antinode locations";
    }

    @Override
    public Integer partTwo() {
        return calculateAntinodes(false, this::inBounds);
    }

    private Integer calculateAntinodes(boolean skipSelfAntinode, Function<Point, Boolean> continueFunc) {
        var antinodes = new HashSet<Point>();
        for (var antennasAtFreq : antennas.values()) {
            for (int i = 0; i < antennasAtFreq.size(); i++) {
                for (int j = 0; j < antennasAtFreq.size(); j++) {
                    if (i == j) continue;
                    antinodes.addAll(calculateForAntenna(antennasAtFreq.get(i), antennasAtFreq.get(j),
                            skipSelfAntinode, continueFunc));
                    antinodes.addAll(calculateForAntenna(antennasAtFreq.get(j), antennasAtFreq.get(i),
                            skipSelfAntinode, continueFunc));
                }
            }
        }
        return antinodes.size();
    }

    private List<Point> calculateForAntenna(Point firstAntenna, Point secondAntenna, boolean skipSelfAntinode,
                                            Function<Point, Boolean> stopFunc) {
        var antinodes = new ArrayList<Point>();
        var period = firstAntenna.minus(secondAntenna);
        var currentAntinode = firstAntenna;
        if (skipSelfAntinode) {
            currentAntinode = currentAntinode.plus(period);
        }
        do {
            if (inBounds(currentAntinode)) {
                antinodes.add(currentAntinode);
            }
            currentAntinode = currentAntinode.plus(period);
        } while (stopFunc.apply(currentAntinode));
        return antinodes;
    }

    boolean inBounds(Point point) {
        return point.x() >= 0 && point.x() < width && point.y() >= 0 && point.y() < length;
    }
}
