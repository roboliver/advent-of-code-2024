package common;

import day01.DayOne;
import day02.DayTwo;
import day03.DayThree;
import day04.DayFour;
import day05.DayFive;
import day06.DaySix;
import day07.DaySeven;
import day08.DayEight;
import day09.DayNine;
import day10.DayTen;
import day11.DayEleven;
import day12.DayTwelve;
import day13.DayThirteen;
import day14.DayFourteen;
import day15.DayFifteen;
import day16.DaySixteen;
import day17.DaySeventeen;
import day18.DayEighteen;
import day19.DayNineteen;
import day20.DayTwenty;
import day21.DayTwentyOne;
import day22.DayTwentyTwo;
import day23.DayTwentyThree;
import day24.DayTwentyFour;

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
    EIGHT(DayEight::new),
    NINE(DayNine::new),
    TEN(DayTen::new),
    ELEVEN(DayEleven::new),
    TWELVE(DayTwelve::new),
    THIRTEEN(DayThirteen::new),
    FOURTEEN(DayFourteen::new),
    FIFTEEN(DayFifteen::new),
    SIXTEEN(DaySixteen::new),
    SEVENTEEN(DaySeventeen::new),
    EIGHTEEN(DayEighteen::new),
    NINETEEN(DayNineteen::new),
    TWENTY(DayTwenty::new),
    TWENTY_ONE(DayTwentyOne::new),
    TWENTY_TWO(DayTwentyTwo::new),
    TWENTY_THREE(DayTwentyThree::new),
    TWENTY_FOUR(DayTwentyFour::new);

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

    public DaySplitType<?, ?> construct(BufferedReader lines) throws IOException {
        return constructor.apply(lines);
    }

    private interface DayConstructor {
        DaySplitType<?, ?> apply(BufferedReader lines) throws IOException;
    }
}
