package day22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayTwentyTwoTest {

    @Test
    void testPartOne() throws IOException {
        var input = """
            1
            10
            100
            2024
            """;
        var sut = new DayTwentyTwo(testLineReader(input));
        assertEquals(37327623L, sut.partOne());
    }

    @Test
    void testPartTwo() throws IOException {
        var input = """
                1
                2
                3
                2024
                """;
        var sut = new DayTwentyTwo(testLineReader(input));
        assertEquals(23, sut.partTwo());
    }
}
