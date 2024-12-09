package day04;

import common.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class DayFour implements Day {

    private final char[][] wordSearch;

    public DayFour(BufferedReader input) throws IOException {
        List<char[]> lines;
        try (input) {
            lines = input.lines()
                    .map(String::toCharArray)
                    .toList();
        }
        this.wordSearch = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            this.wordSearch[i] = lines.get(i);
        }
    }

    @Override
    public String partOneName() {
        return "XMAS matches";
    }

    @Override
    public int partOne() {
        return doPart(XmasArrangement.toLetterPositions(), 'X');

    }

    @Override
    public String partTwoName() {
        return "X-MAS matches";
    }

    @Override
    public int partTwo() {
        return doPart(CrossMasArrangement.toLetterPositions(), 'A');
    }

    private int doPart(List<List<LetterPosition>> arrangements, char startingLetter) {
        var matches = 0;
        for (int row = 0; row < wordSearch.length; row++) {
            for (int col = 0; col < wordSearch.length; col++) {
                var letter = wordSearch[row][col];
                if (letter == startingLetter) {
                    int finalRow = row;
                    int finalCol = col;
                    matches += (int) arrangements.stream()
                            .filter(it -> checkArrangement(it, finalRow, finalCol))
                            .count();
                }
            }
        }
        return matches;
    }

    private boolean checkArrangement(List<LetterPosition> arrangement, int startRow, int startCol) {
        for (var letterPosition : arrangement) {
            var row = startRow + letterPosition.rowAdjust();
            var col = startCol + letterPosition.colAdjust();
            if (row < 0 || row >= wordSearch.length ||
                    col < 0 || col >= wordSearch[0].length) {
                return false;
            }
            if (wordSearch[row][col] != letterPosition.letter()) {
                return false;
            }
        }
        return true;
    }
}
