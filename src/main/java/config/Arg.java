package config;

import java.util.Optional;

public enum Arg {
    DAY("d", "day", true),
    VERIFY("v", "verify-output", false);

    private final String nameShort;
    private final String nameLong;
    private final boolean hasOption;

    Arg(String nameShort, String nameLong, boolean hasOption) {
        this.nameShort = nameShort;
        this.nameLong = nameLong;
        this.hasOption = hasOption;
    }

    public static Optional<Arg> getByStr(String str) {
        for (var arg : values()) {
            if (str.equals("-" + arg.nameShort) || str.equals("--" + arg.nameLong)) {
                return Optional.of(arg);
            }
        }
        return Optional.empty();
    }

    public boolean hasOption() {
        return hasOption;
    }
}
