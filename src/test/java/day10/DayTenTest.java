package day10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayTenTest {

    private static final String INPUT = """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
            """;
    private DayTen sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayTen(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(36, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(81, sut.partTwo());
    }
}
