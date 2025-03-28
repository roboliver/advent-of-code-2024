package day25;

import config.DaySplitType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayTwentyFive implements DaySplitType<Integer, String> {

    private final List<Combination> locks;
    private final List<Combination> keys;

    public DayTwentyFive(BufferedReader input) throws IOException {
        var locks = new ArrayList<Combination>();
        var keys = new ArrayList<Combination>();
        String line;
        try (input) {
            int[] columns = null;
            boolean isLock = false;
            int rowsRead = 0;
            while ((line = input.readLine()) != null) {
                if (columns == null && !line.isEmpty()) {
                    columns = new int[5];
                    isLock = line.equals("#####");
                } else if (rowsRead == 5) {
                    var combination = new Combination(columns);
                    if (isLock) {
                        locks.add(combination);
                    } else {
                        keys.add(combination);
                    }
                    columns = null;
                    rowsRead = 0;
                } else if (columns != null) {
                    for (int i = 0; i < 5; i++) {
                        if (line.charAt(i) == '#') {
                            columns[i]++;
                        }
                    }
                    rowsRead++;
                }
            }
        }
        this.keys = Collections.unmodifiableList(keys);
        this.locks = Collections.unmodifiableList(locks);
    }

    @Override
    public String partOneName() {
        return "unique lock/key pair fits";
    }

    @Override
    public Integer partOne() {
        int fittingCombos = 0;
        for (var key : keys) {
            for (var lock : locks) {
                if (key.fits(lock)) {
                    fittingCombos++;
                }
            }
        }
        return fittingCombos;
    }

    @Override
    public String partTwoName() {
        return "chronicle delivered!";
    }

    @Override
    public String partTwo() {
        return "\uD83D\uDD5B\uD83D\uDCDC\uD83D\uDEF7\uD83C\uDF84";
    }
}
