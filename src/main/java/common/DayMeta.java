package common;

import day01.DayOne;

import java.io.BufferedReader;
import java.io.IOException;

public enum DayMeta {
    TODO(it -> {
            throw new IllegalStateException("Day is still todo, cannot be inited");
        }, true),
    ONE(DayOne::new);

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

    public Day construct(BufferedReader lines) throws IOException {
        return constructor.apply(lines);
    }

    private interface DayConstructor {
        Day apply(BufferedReader lines) throws IOException;
    }
}
