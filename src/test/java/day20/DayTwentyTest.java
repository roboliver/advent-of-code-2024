package day20;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayTwentyTest {

    private static final String INPUT = """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
            """;

    @Test
    void testPartOne() throws IOException {
        var sut = new DayTwenty(testLineReader(INPUT), 1);
        assertEquals(44, sut.partOne());
    }

    @Test
    void testPartTwo() throws IOException {
        var sut = new DayTwenty(testLineReader(INPUT), 50);
        assertEquals(285, sut.partTwo());
    }

}
