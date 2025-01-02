package day19;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayNineteenTest {

    private static final String INPUT = """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
            """;
    private DayNineteen sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayNineteen(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(6L, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(16L, sut.partTwo());
    }
}
