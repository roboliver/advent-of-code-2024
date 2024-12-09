package day02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayTwoTest {

    private static final String INPUT = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """;
    private DayTwo sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayTwo(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(2, sut.partOne());
    }

    @Test
    void testPartTwo() throws IOException {
        assertEquals(4, sut.partTwo());
    }
}