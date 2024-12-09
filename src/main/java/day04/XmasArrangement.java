package day04;

import java.util.Arrays;
import java.util.List;

/**
 * Positions of possible arrangements of 'XMAS' relative to a starting 'X'.
 */
public enum XmasArrangement {
    UP(-1, 0, -2, 0, -3, 0),
    RIGHT(0, 1, 0, 2, 0, 3),
    DOWN(1, 0, 2, 0, 3, 0),
    LEFT(0, -1, 0, -2, 0, -3),
    UP_RIGHT(-1, 1, -2, 2, -3, 3),
    DOWN_RIGHT(1, 1, 2, 2, 3, 3),
    DOWN_LEFT(1, -1, 2, -2, 3, -3),
    UP_LEFT(-1, -1, -2, -2, -3, -3);

    private final int mRow;
    private final int mCol;
    private final int aRow;
    private final int aCol;
    private final int sRow;
    private final int sCol;

    XmasArrangement(int mRow, int mCol, int aRow, int aCol, int sRow, int sCol) {
        this.mRow = mRow;
        this.mCol = mCol;
        this.aRow = aRow;
        this.aCol = aCol;
        this.sRow = sRow;
        this.sCol = sCol;
    }

    private List<LetterPosition> convert() {
        return List.of(
                new LetterPosition(mRow, mCol, 'M'),
                new LetterPosition(aRow, aCol, 'A'),
                new LetterPosition(sRow, sCol, 'S'));
    }

    public static List<List<LetterPosition>> toLetterPositions() {
        return Arrays.stream(values())
                .map(XmasArrangement::convert)
                .toList();
    }
}
