package day25;

import java.util.Arrays;
import java.util.stream.IntStream;

public record Combination(int[] columns) {

    public Combination(int[] columns) {
        if (columns.length != 5) {
            throw new IllegalArgumentException("must have 5 columns");
        }
        this.columns = Arrays.copyOf(columns, 5);
    }

    public boolean fits(Combination other) {
        return IntStream.range(0, 5)
                .allMatch(i -> columns[i] + other.columns[i] <= 5);
    }
}
