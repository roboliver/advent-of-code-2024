package day24;

import config.DaySplitType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class DayTwentyFour implements DaySplitType<Long, String> {

    private final Map<String, Boolean> liveWires;
    private final List<Gate> gates;

    public DayTwentyFour(BufferedReader input) throws IOException {
        var liveWires = new HashMap<String, Boolean>();
        var gates = new ArrayList<Gate>();
        String line;
        try (input) {
            while (!(line = input.readLine()).isEmpty()) {
                var split = line.split(": ");
                liveWires.put(split[0], split[1].equals("1"));
            }
            while ((line = input.readLine()) != null) {
                gates.add(parseGateLine(line));
            }
        }
        this.liveWires = Collections.unmodifiableMap(liveWires);
        this.gates = Collections.unmodifiableList(gates);
    }

    private Gate parseGateLine(String line) {
        var splitInOut = line.split(" -> ");
        var out = splitInOut[1];
        var splitInLeftRight = splitInOut[0].split(" ");
        var leftIn = splitInLeftRight[0];
        var rightIn = splitInLeftRight[2];
        var operator = splitInLeftRight[1];
        return new Gate(leftIn, rightIn, operator, out);
    }

    @Override
    public String partOneName() {
        return "decimal output";
    }

    @Override
    public Long partOne() {
        var liveGates = new LiveGates(this.liveWires, this.gates);
        return liveGates.calculateOutput();
    }

    @Override
    public String partTwoName() {
        return "sorted swapped output wires";
    }

    @Override
    public String partTwo() {
        var rewireableGates = new RewireableGates(this.gates);
        return rewireableGates.fixSwappedOutputs();
    }
}
