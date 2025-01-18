package day23;

import common.DaySplitType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DayTwentyThree implements DaySplitType<Integer, String> {

    private final Map<String, Connected> connections;

    public DayTwentyThree(BufferedReader input) throws IOException {
        var connections = new HashMap<String, List<String>>();
        try (input) {
            String line;
            while ((line = input.readLine()) != null) {
                var computers = line.split("-");
                addConnection(connections, computers[0], computers[1]);
            }
        }
        this.connections = connections.entrySet().stream()
                .map(it -> new AbstractMap.SimpleEntry<>(it.getKey(), new Connected(it.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void addConnection(Map<String, List<String>> connections, String a, String b) {
        var from = a.compareTo(b) < 0 ? a : b;
        var to = from.equals(a) ? b : a;
        var connected = connections.computeIfAbsent(from, k -> new ArrayList<>());
        connected.add(to);
    }

    @Override
    public String partOneName() {
        return "interconnected trios with a t_";
    }

    @Override
    public Integer partOne() {
        int triosWithTs = 0;
        for (var first : connections.keySet()) {
            var firstConnected = getConnected(first);
            for (var second : firstConnected) {
                var secondConnected = getConnected(second);
                for (var third : secondConnected) {
                    if (firstConnected.contains(third) &&
                            anyStartWithT(first, second, third)) {
                            triosWithTs++;
                    }
                }
            }
        }
        return triosWithTs;
    }

    @Override
    public String partTwoName() {
        return "LAN party password";
    }

    @Override
    public String partTwo() {
        int prospectiveLanParty = 1;
        String password = null;
        var network = new ArrayDeque<IndexedComputer>();
        for (var start : connections.keySet()) {
            network.push(new IndexedComputer(start, 0));
            while (!network.isEmpty()) {
                if (fullyConnected(network)) {
                    if (network.size() > prospectiveLanParty) {
                        prospectiveLanParty = network.size();
                        password = password(network);
                    }
                    var indexedComputer = network.pop();
                    var computer = indexedComputer.computer();
                    var index = indexedComputer.index();
                    if (index < getConnected(computer).size()) {
                        network.push(new IndexedComputer(computer, index + 1));
                        network.push(new IndexedComputer(connections.get(computer).get(index), 0));
                    }
                } else {
                    network.pop();
                }
            }
        }
        return password;
    }

    private List<String> getConnected(String from) {
        return Optional.ofNullable(connections.get(from))
                .map(Connected::getAll)
                .orElse(List.of());
    }

    private boolean fullyConnected(Collection<IndexedComputer> network) {
        var sortedNetwork = network.stream()
                .map(IndexedComputer::computer)
                .sorted()
                .toList();
        for (int from = 0; from < sortedNetwork.size() - 1; from++) {
            var connected = connections.get(sortedNetwork.get(from));
            for (int to = from + 1; to < sortedNetwork.size(); to++) {
                if (!connected.contains(sortedNetwork.get(to))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean anyStartWithT(String first, String second, String third) {
        return first.startsWith("t") || second.startsWith("t") || third.startsWith("t");
    }

    private String password(Collection<IndexedComputer> computers) {
        return computers.stream()
                .map(IndexedComputer::computer)
                .sorted()
                .collect(Collectors.joining(","));
    }
}
