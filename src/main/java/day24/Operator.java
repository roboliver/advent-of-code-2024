package day24;

import java.util.Arrays;

public enum Operator {
    AND,
    XOR,
    OR;

    public static Operator fromString(String str) {
        return Arrays.stream(values())
                .filter(it -> it.toString().equals(str))
                .findFirst()
                .orElseThrow();
    }
}
