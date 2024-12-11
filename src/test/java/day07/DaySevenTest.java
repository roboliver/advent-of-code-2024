package day07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DaySevenTest {

    private static final String INPUT = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """;
    private DaySeven sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DaySeven(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(3749, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(11387, sut.partTwo());
    }
}
