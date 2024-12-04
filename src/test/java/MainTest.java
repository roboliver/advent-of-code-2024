import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    @Test
    public void testHello() {
        assertEquals("Hello world!", Main.hello());
    }

}
