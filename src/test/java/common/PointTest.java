package common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointTest {

    @Test
    void testRotate() {
        var point = new Point(5, 3);
        assertEquals(new Point(3, -5), point.rotate(Point.Turn.QUARTER));
        assertEquals(new Point(-5, -3), point.rotate(Point.Turn.HALF));
        assertEquals(new Point(-3, 5), point.rotate(Point.Turn.THREE_QUARTER));
    }
}
