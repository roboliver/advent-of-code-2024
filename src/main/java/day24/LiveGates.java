package day24;

import java.util.*;

public class LiveGates {

    private final Map<String, Boolean> liveWires;
    private final Map<String, List<LiveGate>> gatesByInputWire;

    public LiveGates(Map<String, Boolean> liveWires, List<Gate> gates) {
        this.liveWires = new HashMap<>(liveWires);
        var gatesByInputWire = new HashMap<String, List<LiveGate>>();
        for (var gate : gates) {
            var liveGate = new LiveGate(gate.operator(), gate.output());
            var leftGates = gatesByInputWire.computeIfAbsent(gate.leftInput(), it -> new ArrayList<>());
            var rightGates = gatesByInputWire.computeIfAbsent(gate.rightInput(), it -> new ArrayList<>());
            leftGates.add(liveGate);
            rightGates.add(liveGate);
        }
        this.gatesByInputWire = Collections.unmodifiableMap(gatesByInputWire);
    }

    public long calculateOutput() {
        long result = 0L;
        var liveWires = new HashMap<>(this.liveWires);
        while (!liveWires.isEmpty()) {
            var liveWiresNew = new HashMap<String, Boolean>();
            for (var liveWire : liveWires.entrySet()) {
                for (var gate : gatesByInputWire.get(liveWire.getKey())) {
                    gate.setInput(liveWire.getValue());
                    if (gate.isReady()) {
                        if (gate.getOutputWire().startsWith("z")) {
                            int position = Integer.parseInt(gate.getOutputWire().substring(1));
                            long bit = gate.run() ? 1L : 0L;
                            result |= (bit << position);
                        } else {
                            liveWiresNew.put(gate.getOutputWire(), gate.run());
                        }
                    }
                }
            }
            liveWires = liveWiresNew;
        }
        return result;
    }
}
