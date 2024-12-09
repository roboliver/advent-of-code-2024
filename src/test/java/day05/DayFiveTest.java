package day05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayFiveTest {

    private static final String INPUT = """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13
            
            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
            """;
    private DayFive sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayFive(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(143, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals(123, sut.partTwo());
    }
}
