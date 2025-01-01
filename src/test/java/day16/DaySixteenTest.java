package day16;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DaySixteenTest {

    private static final String INPUT_SMALL = """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
            """;
    private static final String INPUT_LARGE = """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
            """;
    private DaySixteen sutSmall;
    private DaySixteen sutLarge;

    @BeforeEach
    void setup() throws IOException {
        sutSmall = new DaySixteen(testLineReader(INPUT_SMALL));
        sutLarge = new DaySixteen(testLineReader(INPUT_LARGE));
    }

    @Test
    void testPartOne() {
        assertEquals(7036, sutSmall.partOne());
        assertEquals(11048, sutLarge.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(45, sutSmall.partTwo());
        assertEquals(64, sutLarge.partTwo());
    }

}
