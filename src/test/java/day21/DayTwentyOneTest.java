package day21;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayTwentyOneTest {

    private static final String INPUT = """
            029A
            980A
            179A
            456A
            379A
            """;
    private DayTwentyOne sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayTwentyOne(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(126384, sut.partOne());
    }
}
