package day11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayElevenTest {

    private static final String INPUT = "125 17";
    private DayEleven sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayEleven(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(55312L, sut.partOne());
    }
}
