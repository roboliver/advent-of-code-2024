package common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReaderUtils {

    /**
     * Returns a reader for the default input file name.
     *
     * @param day The Advent of Code day the input file is for.
     * @return The file reader.
     * @throws FileNotFoundException If the file doesn't exist.
     */
    public static BufferedReader inputLineReader(int day) throws FileNotFoundException {
        return inputLineReader("src/main/resources/day" + String.format("%02d", day) + ".txt");
    }

    /**
     * Returns a reader for the specified file name.
     *
     * @param inputFile The file name.
     * @return The file reader.
     * @throws FileNotFoundException If the file doesn't exist.
     */
    public static BufferedReader inputLineReader(String inputFile) throws FileNotFoundException {
        return new BufferedReader(new FileReader(inputFile));
    }
}
