package day08;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayEightTest {

    private static final String INPUT = """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
            """;
    private DayEight sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayEight(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(14, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(34, sut.partTwo());
    }
}
