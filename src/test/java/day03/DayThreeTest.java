package day03;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayThreeTest {

    @Test
    void testPartOne() throws IOException {
        var input = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
        var sut = new DayThree(testLineReader(input));
        assertEquals(161, sut.partOne());
    }

    @Test
    void testPartTwo() throws IOException {
        var input = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";
        var sut = new DayThree(testLineReader(input));
        assertEquals(48, sut.partTwo());
    }
}
