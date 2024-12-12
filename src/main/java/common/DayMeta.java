package common;

import day01.DayOne;
import day02.DayTwo;
import day03.DayThree;
import day04.DayFour;
import day05.DayFive;
import day06.DaySix;
import day07.DaySeven;
import day08.DayEight;

import java.io.BufferedReader;
import java.io.IOException;

public enum DayMeta {
    TODO(it -> {
            throw new IllegalStateException("Day is still todo, cannot be inited");
        }, true),
    ONE(DayOne::new),
    TWO(DayTwo::new),
    THREE(DayThree::new),
    FOUR(DayFour::new),
    FIVE(DayFive::new),
    SIX(DaySix::new),
    SEVEN(DaySeven::new),
    EIGHT(DayEight::new);

    private final DayConstructor constructor;
    private final boolean isTodo;

    DayMeta(DayConstructor constructor) {
        this(constructor, false);
    }

    DayMeta(DayConstructor constructor, boolean isTodo) {
        this.constructor = constructor;
        this.isTodo = isTodo;
    }

    public boolean isTodo() {
        return this.isTodo;
    }

    public Day<?> construct(BufferedReader lines) throws IOException {
        return constructor.apply(lines);
    }

    private interface DayConstructor {
        Day<?> apply(BufferedReader lines) throws IOException;
    }
}
