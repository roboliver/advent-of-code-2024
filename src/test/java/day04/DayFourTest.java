package day04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayFourTest {

    private static final String INPUT = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
            """;
    private DayFour sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayFour(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(18, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(9, sut.partTwo());
    }
}
