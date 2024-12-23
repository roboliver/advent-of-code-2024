package day09;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayNineTest {

    private static final String INPUT = "2333133121414131402";
    private DayNine sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayNine(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(1928L, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(2858L, sut.partTwo());
    }
}
