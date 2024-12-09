package day04;

import java.util.Arrays;
import java.util.List;

/**
 * Positions of possible arrangements of an 'X-MAS' pattern relative to a starting 'A'.
 */
public enum CrossMasArrangement {
    LEFT_TO_RIGHT(-1, -1, -1, 1,
            1, -1, 1, 1),
    TOP_TO_BOTTOM(-1, -1, 1, -1,
            -1, 1, 1, 1),
    RIGHT_TO_LEFT(-1, 1, -1, -1,
            1, 1, 1, -1),
    BOTTOM_TO_TOP(1, -1, -1, -1,
            1, 1, -1, 1);

    private final int m1Row;
    private final int m1Col;
    private final int s1Row;
    private final int s1Col;
    private final int m2Row;
    private final int m2Col;
    private final int s2Row;
    private final int s2Col;

    CrossMasArrangement(int m1Row, int m1Col, int s1Row, int s1Col,
                        int m2Row, int m2Col, int s2Row, int s2Col) {
        this.m1Row = m1Row;
        this.m1Col = m1Col;
        this.s1Row = s1Row;
        this.s1Col = s1Col;
        this.m2Row = m2Row;
        this.m2Col = m2Col;
        this.s2Row = s2Row;
        this.s2Col = s2Col;
    }

    private List<LetterPosition> convert() {
        return List.of(
                new LetterPosition(m1Row, m1Col, 'M'),
                new LetterPosition(s1Row, s1Col, 'S'),
                new LetterPosition(m2Row, m2Col, 'M'),
                new LetterPosition(s2Row, s2Col, 'S'));
    }

    public static List<List<LetterPosition>> toLetterPositions() {
        return Arrays.stream(values())
                .map(CrossMasArrangement::convert)
                .toList();
    }
}
