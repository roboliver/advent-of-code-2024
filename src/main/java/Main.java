import config.Answers;
import config.Arg;
import config.DayMeta;
import config.DaySplitType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static common.ExceptionUtils.exceptionStackTrace;
import static config.DayMeta.*;
import static config.ReaderUtils.inputLineReader;

public class Main {

    private static final List<DayMeta> DAYS = List.of(
            ONE, TWO, THREE, FOUR, FIVE,
            SIX, SEVEN, EIGHT, NINE, TEN,
            ELEVEN, TWELVE, THIRTEEN, FOURTEEN, FIFTEEN,
            SIXTEEN, SEVENTEEN, EIGHTEEN, NINETEEN, TWENTY,
            TWENTY_ONE, TWENTY_TWO, TWENTY_THREE, TWENTY_FOUR, TWENTY_FIVE
    );

    public static void main(String[] args) throws IOException {
        var argsParsed = parseArgs(args);
        boolean verifyOutput = argsParsed.containsKey(Arg.VERIFY);
        Map<Integer, Answers> answersByDay;
        if (verifyOutput) {
            answersByDay = readAnswersFile();
        } else {
            answersByDay = Map.of();
        }
        if (argsParsed.containsKey(Arg.DAY)) {
            int day = Integer.parseInt(argsParsed.get(Arg.DAY));
            int correctCount = runDay(day, verifyOutput, answersByDay);
            if (verifyOutput) {
                printCorrectCount(correctCount, 2);
            }
        } else {
            int correctCount = 0;
            for (int i = 0; i < DAYS.size(); i++) {
                correctCount += runDay(i + 1, verifyOutput, answersByDay);
            }
            if (verifyOutput) {
                printCorrectCount(correctCount, DAYS.size() * 2);
            }
        }
    }

    private static Map<Arg, String> parseArgs(String[] args) {
        var argsMap = new HashMap<Arg, String>();
        for (int i = 0; i < args.length; i++) {
            var argOptional = Arg.getByStr(args[i]);
            if (argOptional.isPresent()) {
                var arg = argOptional.get();
                if (arg.hasOption()) {
                    argsMap.put(arg, args[i + 1]);
                    i++;
                } else {
                    argsMap.put(arg, "");
                }
            }
        }
        return argsMap;
    }

    private static int runDay(int dayNum, boolean verifyOutput, Map<Integer, Answers> answersByDay) {
        long msBefore = System.currentTimeMillis();
        System.out.println("\n======\nDay " + String.format("%02d", dayNum) + "\n======");
        var dayMeta = DAYS.get(dayNum - 1); // get zero indexed day
        if (dayMeta.isTodo()) {
            System.out.println("TODO");
            return 0;
        }
        DaySplitType<?, ?> day;
        try {
            var lines = inputLineReader(dayNum);
            day = dayMeta.construct(lines);
        } catch (Exception e) {
            System.out.println("Exception thrown constructing day:");
            System.out.println(exceptionStackTrace(e));
            return 0;
        }
        int correctCount = 0;
        correctCount += printPart("one", day.partOneName(), day::partOne,
                verifyOutput, () -> answersByDay.get(dayNum), Answers::partOne, Answers::checkPartOne);
        correctCount += printPart("two", day.partTwoName(), day::partTwo,
                verifyOutput, () -> answersByDay.get(dayNum), Answers::partTwo, Answers::checkPartTwo);
        long msAfter = System.currentTimeMillis();
        System.out.println("Time taken: " + (msAfter - msBefore) + "ms");
        return correctCount;
    }

    private static <T> int printPart(String part, String partName, Supplier<T> partMethod,
                                      boolean verifyOutput, Supplier<Answers> answersSupplier,
                                      Function<Answers, Object> expectedFunc, BiFunction<Answers, Object, Boolean> verifyFunc) {
        try {
            int correct;
            var result = partMethod.get();
            System.out.println("Part " + part + " (" + partName + "): " + result);
            if (verifyOutput) {
                var answers = answersSupplier.get();
                var expected = expectedFunc.apply(answers);
                var isCorrect = verifyFunc.apply(answers, result);
                System.out.println("Expected: " + expected + "........" + (isCorrect ? "CORRECT" : "WRONG"));
                correct = isCorrect ? 1 : 0;
            } else {
                correct = 1;
            }
            System.out.println();
            return correct;
        } catch (Exception e) {
            System.out.println("Exception thrown in part " + part + ":");
            System.out.println(exceptionStackTrace(e));
            return 0;
        }
    }

    private static void printCorrectCount(int correctCount, int total) {
        System.out.println("CORRECT ANSWERS: " + correctCount + "/" + total);
    }

    private static Map<Integer, Answers> readAnswersFile() throws IOException {
        var answersByDay = new HashMap<Integer, Answers>();
        try (var lines = inputLineReader("src/main/resources/answers.txt")) {
            for (int i = 0; i < DAYS.size(); i++) {
                var partOneAnswer = parseAnswer(lines.readLine());
                var partTwoAnswer = parseAnswer(lines.readLine());
                answersByDay.put(i + 1, new Answers(partOneAnswer, partTwoAnswer));
            }
        }
        return answersByDay;
    }

    private static Object parseAnswer(String answer) {
        var split = answer.split(":");
        var type = split[1].trim();
        var value = split[2].trim();
        return switch (type) {
            case "i" -> Integer.parseInt(value);
            case "l" -> Long.parseLong(value);
            case "s" -> value;
            default -> throw new IllegalStateException("unknown type");
        };
    }

}