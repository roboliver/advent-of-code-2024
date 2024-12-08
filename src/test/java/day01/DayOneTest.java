package day01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayOneTest {

    private static final String INPUT = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """;
    private DayOne sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayOne(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(11, sut.partOne());
    }

    @Test
    void testPartTwo() throws IOException {
        assertEquals(31, sut.partTwo());
    }
}
