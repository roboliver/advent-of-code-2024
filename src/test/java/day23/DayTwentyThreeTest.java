package day23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.testLineReader;

public class DayTwentyThreeTest {

    private static final String INPUT = """
            kh-tc
            qp-kh
            de-cg
            ka-co
            yn-aq
            qp-ub
            cg-tb
            vc-aq
            tb-ka
            wh-tc
            yn-cg
            kh-ub
            ta-co
            de-co
            tc-td
            tb-wq
            wh-td
            ta-ka
            td-qp
            aq-cg
            wq-ub
            ub-vc
            de-ta
            wq-aq
            wq-vc
            wh-yn
            ka-de
            kh-ta
            co-tc
            wh-qp
            tb-vc
            td-yn
            """;
    private DayTwentyThree sut;

    @BeforeEach
    void setup() throws IOException {
        sut = new DayTwentyThree(testLineReader(INPUT));
    }

    @Test
    void testPartOne() {
        assertEquals(7, sut.partOne());
    }

    @Test
    void testPartTwo() {
        assertEquals("co,de,ka,ta", sut.partTwo());
    }
}
