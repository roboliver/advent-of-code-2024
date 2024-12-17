package day09;

import common.Day;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayNineTest {

    private static final String INPUT = "2333133121414131402";

    // 22...333...1...333.22.4444.4444.333.444422
    // .4444.44

    //2222433344413333334224444444
    // 222243334441333333422444444
    // 222243334441333333422444444

    private DayNine sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayNine(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(1928L, sut.partOne());
    }
}
