package config;

public record Answers(Object partOne, Object partTwo) {

    public boolean checkPartOne(Object result) {
        return partOne.equals(result);
    }

    public boolean checkPartTwo(Object result) {
        return partTwo.equals(result);
    }
}
