package day17;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DaySeventeenTest {

    @Test
    void testPartOne() throws IOException {
        var input = """
            Register A: 729
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
            """;
        var sut = new DaySeventeen(testLineReader(input));
        assertEquals("4,6,3,5,6,3,5,2,1,0", sut.partOne());
    }

    @Test
    void testPartTwo() throws IOException {
        var input = """
                Register A: 2024
                Register B: 0
                Register C: 0

                Program: 0,3,5,4,3,0
                """;
        var sut = new DaySeventeen(testLineReader(input));
        assertEquals(117440, sut.partTwo());
    }
}
