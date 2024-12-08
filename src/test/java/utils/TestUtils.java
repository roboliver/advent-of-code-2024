package utils;

import java.io.BufferedReader;
import java.io.StringReader;

public class TestUtils {

    /**
     * Returns a reader that will return the specified text, for use in testing.
     *
     * @param sampleText The text to return from the reader.
     * @return The reader.
     */
    public static BufferedReader testLineReader(String sampleText) {
        return new BufferedReader(new StringReader(sampleText));
    }
}
