import common.DayMeta;
import common.DaySplitType;
import common.ReaderUtils;

import java.util.List;
import java.util.function.Supplier;

import static common.DayMeta.*;
import static common.ExceptionUtils.exceptionStackTrace;

public class Main {

    private static final List<DayMeta> DAYS = List.of(
            ONE, TWO, THREE, FOUR, FIVE,
            SIX, SEVEN, EIGHT, NINE, TEN,
            ELEVEN, TWELVE, THIRTEEN, FOURTEEN, FIFTEEN,
            SIXTEEN, SEVENTEEN, EIGHTEEN, NINETEEN, TWENTY,
            TWENTY_ONE, TWENTY_TWO, TWENTY_THREE, TWENTY_FOUR, TWENTY_FIVE
    );

    public static void main(String[] args) {
        if (args.length > 0) {
            var day = Integer.parseInt(args[0]);
            runDay(day);
        } else {
            for (int i = 1; i <= DAYS.size(); i++) {
                runDay(i);
            }
        }
    }

    private static void runDay(int dayNum) {
        long msBefore = System.currentTimeMillis();
        System.out.println("\n======\nDay " + String.format("%02d", dayNum) + "\n======");
        var dayMeta = DAYS.get(dayNum - 1); // get zero indexed day
        if (dayMeta.isTodo()) {
            System.out.println("TODO");
            return;
        }
        DaySplitType<?, ?> day;
        try {
            var lines = ReaderUtils.inputLineReader(dayNum);
            day = dayMeta.construct(lines);
        } catch (Exception e) {
            System.out.println("Exception thrown constructing day:");
            System.out.println(exceptionStackTrace(e));
            return;
        }
        printPart("one", day.partOneName(), day::partOne);
        printPart("two", day.partTwoName(), day::partTwo);
        long msAfter = System.currentTimeMillis();
        System.out.println("Time taken: " + (msAfter - msBefore) + "ms");
    }

    private static <T> void printPart(String part, String partName, Supplier<T> partMethod) {
        try {
            var result = partMethod.get();
            System.out.println("Part " + part + " (" + partName + "): " + result);
        } catch (Exception e) {
            System.out.println("Exception thrown in part " + part + ":");
            System.out.println(exceptionStackTrace(e));
        }
    }

}