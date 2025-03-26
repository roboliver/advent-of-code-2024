package day25;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayTwentyFiveTest {

    private static final String INPUT = """
            #####
            .####
            .####
            .####
            .#.#.
            .#...
            .....

            #####
            ##.##
            .#.##
            ...##
            ...#.
            ...#.
            .....

            .....
            #....
            #....
            #...#
            #.#.#
            #.###
            #####

            .....
            .....
            #.#..
            ###..
            ###.#
            ###.#
            #####

            .....
            .....
            .....
            #....
            #.#..
            #.#.#
            #####
            """;
    private DayTwentyFive sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayTwentyFive(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(3, sut.partOne());
    }
}
