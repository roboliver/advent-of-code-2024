package day12;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayTwelveTest {

    private static final String INPUT = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """;
    private DayTwelve sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayTwelve(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(1930, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(1206, sut.partTwo());
    }
}
