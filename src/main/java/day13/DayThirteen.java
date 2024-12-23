package day13;

import common.Day;
import common.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayThirteen implements Day<Long> {

    private final List<ClawMachine> clawMachines;

    public DayThirteen(BufferedReader input) throws IOException {
        var clawMachines = new ArrayList<ClawMachine>();
        try (input) {
            boolean hasLines = true;
            while (hasLines) {
                var buttonA = input.readLine();
                var buttonB = input.readLine();
                var prize = input.readLine();
                clawMachines.add(parse(buttonA, buttonB, prize));
                hasLines = (input.readLine()) != null;
            }
        }
        this.clawMachines = Collections.unmodifiableList(clawMachines);
    }

    private ClawMachine parse(String buttonAStr, String buttonBStr, String prizeStr) {
        var buttonA = parseLine(buttonAStr);
        var buttonB = parseLine(buttonBStr);
        var prize = parseLine(prizeStr);
        return new ClawMachine(buttonA.x(), buttonA.y(), buttonB.x(), buttonB.y(), prize.x(), prize.y());
    }

    private Point parseLine(String line) {
        var parts = line.split(": ")[1]
                .split(", ");
        int x = Integer.parseInt(parts[0].substring(2));
        int y = Integer.parseInt(parts[1].substring(2));
        return new Point(x, y);
    }

    @Override
    public String partOneName() {
        return "fewest tokens needed";
    }

    @Override
    public Long partOne() {
        return clawMachines.stream()
                .map(it -> tokenSpend(it, false))
                .mapToLong(it -> it)
                .sum();
    }

    @Override
    public String partTwoName() {
        return "actual fewest tokens needed";
    }

    @Override
    public Long partTwo() {
        return clawMachines.stream()
                .map(it -> tokenSpend(it, true))
                .mapToLong(it -> it)
                .sum();
    }

    private long tokenSpend(ClawMachine clawMachine, boolean unitCorrection) {
        long aX = clawMachine.aX();
        long aY = clawMachine.aY();
        long bX = clawMachine.bX();
        long bY = clawMachine.bY();
        long prizeX = clawMachine.prizeX();
        long prizeY = clawMachine.prizeY();
        if (unitCorrection) {
            prizeX += 10000000000000L;
            prizeY += 10000000000000L;
        }
        long aNumerator = ((bX * prizeY) - (bY * prizeX));
        long aDenominator = ((aY * bX) - (aX * bY));
        long bNumerator = ((aX * prizeY) - (aY * prizeX));
        long bDenominator = ((aX * bY) - (aY * bX));
        if (aNumerator % aDenominator != 0L || bNumerator % bDenominator != 0L) {
            return 0L;
        }
        long aPresses = aNumerator / aDenominator;
        long bPresses = bNumerator / bDenominator;
        if (aPresses < 1L || bPresses < 1L ||
                (!unitCorrection && (aPresses > 100L || bPresses > 100L))) {
            return 0L;
        }
        return ((aPresses * 3L) + bPresses);
    }
}
