package day06;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DaySixTest {

    private static final String INPUT = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
            """;
    private DaySix sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DaySix(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(41, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(6, sut.partTwo());
    }
}
