package day18;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayEighteenTest {

    private static final String INPUT = """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0
            """;
    private DayEighteen sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayEighteen(testLineReader(INPUT), 7, 7);
    }

    @Test
    void testPartOne() {
        assertEquals(22, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals("6,1", sut.partTwo());
    }
}
