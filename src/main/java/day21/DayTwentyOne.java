package day21;

import common.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DayTwentyOne implements Day<Long> {

    private static final char[][] NUMERICAL_KEYPAD = {
            {'7', '8', '9'},
            {'4', '5', '6'},
            {'1', '2', '3'},
            {' ', '0', 'A'}
    };
    private static final char[][] DIRECTIONAL_KEYPAD = {
            {' ', '^', 'A'},
            {'<', 'v', '>'}
    };
    private static final Map<Character, Position> NUMERICAL_KEYMAP = keypadToMap(NUMERICAL_KEYPAD);
    private static final Map<Character, Position> DIRECTIONAL_KEYMAP = keypadToMap(DIRECTIONAL_KEYPAD);

    private final List<String> codes;

    public DayTwentyOne(BufferedReader input) throws IOException {
        try (input) {
            codes = input.lines().toList();
        }
    }

    @Override
    public String partOneName() {
        return "code complexity sum (2 robot-controlled directional keypads)";
    }

    @Override
    public Long partOne() {
        var costsAfter = DirectionalRoute.shortestSequenceLengths(2);
        return codes.stream()
                .mapToLong(it -> codeComplexity(it, costsAfter))
                .sum();
    }

    @Override
    public String partTwoName() {
        return "code complexity sum (25 robot-controlled directional keypads)";
    }

    @Override
    public Long partTwo() {
        var costsAfter = DirectionalRoute.shortestSequenceLengths(25);
        return codes.stream()
                .mapToLong(it -> codeComplexity(it, costsAfter))
                .sum();
    }

    private long codeComplexity(String code, Map<DirectionalRoute, Long> costsAfter) {
        return shortestSequenceLengthNumerical(code, costsAfter) * Integer.parseInt(code.substring(0, code.length() - 1));
    }

    private long shortestSequenceLengthNumerical(String code, Map<DirectionalRoute, Long> costsAfter) {
        return shortestSequenceLength(code, NUMERICAL_KEYMAP, it -> shortestSequenceLengthDirectional(it, costsAfter));
    }

    private long shortestSequenceLengthDirectional(String code, Map<DirectionalRoute, Long> costsAfter) {
        return shortestSequenceLength(code, DIRECTIONAL_KEYMAP, it -> costsAfter.get(DirectionalRoute.fromCode(it)));
    }

    private long shortestSequenceLength(String code, Map<Character, Position> keymap, Function<String, Long> shortestSequenceFunc) {
        long sum = 0L;
        var avoid = keymap.get(' ');
        for (int i = 0; i < code.length(); i++) {
            var from = (i == 0) ? keymap.get('A') : keymap.get(code.charAt(i - 1));
            var to = keymap.get(code.charAt(i));
            sum += findAllSequences(from, to, avoid, "").stream()
                    .mapToLong(shortestSequenceFunc::apply)
                    .min()
                    .orElseThrow();
        }
        return sum;
    }

    private List<String> findAllSequences(Position from, Position to, Position avoid, String curSequence) {
        var sequences = new ArrayList<String>();
        if (from.equals(to)) {
            return List.of(curSequence + "A");
        }
        if (from.row() < to.row() && !from.down().equals(avoid)) {
            sequences.addAll(findAllSequences(from.down(), to, avoid, curSequence + "v"));
        }
        if (from.row() > to.row() && !from.up().equals(avoid)) {
            sequences.addAll(findAllSequences(from.up(), to, avoid, curSequence + "^"));
        }
        if (from.col() < to.col() && !from.right().equals(avoid)) {
            sequences.addAll(findAllSequences(from.right(), to, avoid, curSequence + ">"));
        }
        if (from.col() > to.col() && !from.left().equals(avoid)) {
            sequences.addAll(findAllSequences(from.left(), to, avoid, curSequence + "<"));
        }
        return sequences;
    }

    private static Map<Character, Position> keypadToMap(char[][] keypad) {
        var keyMap = new HashMap<Character, Position>();
        for (int row = 0; row < keypad.length; row++) {
            for (int col = 0; col < keypad[0].length; col++) {
                keyMap.put(keypad[row][col], new Position(row, col));
            }
        }
        return keyMap;
    }

}
