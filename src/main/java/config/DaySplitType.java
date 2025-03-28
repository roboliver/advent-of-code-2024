package config;

public interface DaySplitType<T, U> {

    String partOneName();

    T partOne();

    String partTwoName();

    U partTwo();
}
