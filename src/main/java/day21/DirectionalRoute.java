package day21;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum DirectionalRoute {
    A("A", DirectionalRoute::sequencesA),
    UA("^A", DirectionalRoute::sequencesUA),
    DA("vA", DirectionalRoute::sequencesDA),
    LA("<A", DirectionalRoute::sequencesLA),
    RA(">A", DirectionalRoute::sequencesRA),
    ULA("^<A", DirectionalRoute::sequencesULA),
    LUA("<^A", DirectionalRoute::sequencesLUA),
    URA("^>A", DirectionalRoute::sequencesURA),
    RUA(">^A", DirectionalRoute::sequencesRUA),
    DLA("v<A", DirectionalRoute::sequencesDLA),
    LDA("<vA", DirectionalRoute::sequencesLDA),
    DRA("v>A", DirectionalRoute::sequencesDRA),
    RDA(">vA", DirectionalRoute::sequencesRDA),
    RURA(">^>A", DirectionalRoute::sequencesRURA),
    RRUA(">>^A", DirectionalRoute::sequencesRRUA),
    DLLA("v<<A", DirectionalRoute::sequencesDLLA),
    LDLA("<v<A", DirectionalRoute::sequencesLDLA);

    private final String code;
    private final Supplier<List<List<DirectionalRoute>>> sequences;

    DirectionalRoute(String code, Supplier<List<List<DirectionalRoute>>> sequences) {
        this.code = code;
        this.sequences = sequences;
    }

    private static final Map<String, DirectionalRoute> BY_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(it -> it.code, Function.identity()));

    public static DirectionalRoute fromCode(String code) {
        return BY_CODE.get(code);
    }

    public static Map<DirectionalRoute, Long> shortestSequenceLengths(int robotOperatedDirectionalKeypads) {
        var sequenceLengths = Arrays.stream(values())
                .collect(Collectors.toMap(Function.identity(), it -> (long) it.code.length()));
        for (int i = 0; i < robotOperatedDirectionalKeypads - 1; i++) {
            var sequenceLengthsCopy = new HashMap<>(sequenceLengths);
            sequenceLengths = sequenceLengths.keySet().stream()
                    .collect(Collectors.toMap(
                            Function.identity(), it -> it.shortestSequence(sequenceLengthsCopy)));
        }
        return sequenceLengths;
    }

    private Long shortestSequence(Map<DirectionalRoute, Long> subSequenceLengths) {
        return sequences.get().stream()
                .mapToLong(it -> it.stream()
                        .mapToLong(subSequenceLengths::get)
                        .sum())
                .min()
                .orElseThrow();
    }

    private static List<List<DirectionalRoute>> sequencesA() {
        return List.of(
                List.of(A));
    }

    private static List<List<DirectionalRoute>> sequencesUA() {
        return List.of(
                List.of(LA, RA));
    }

    private static List<List<DirectionalRoute>> sequencesDA() {
        return List.of(
                List.of(DLA, URA),
                List.of(DLA, RUA),
                List.of(LDA, URA),
                List.of(LDA, RUA));
    }

    private static List<List<DirectionalRoute>> sequencesLA() {
        return List.of(
                List.of(DLLA, RURA),
                List.of(DLLA, RRUA),
                List.of(LDLA, RURA),
                List.of(LDLA, RRUA));
    }

    private static List<List<DirectionalRoute>> sequencesRA() {
        return List.of(
                List.of(DA, UA));
    }

    private static List<List<DirectionalRoute>> sequencesULA() {
        return List.of(
                List.of(LA, DLA, RURA),
                List.of(LA, DLA, RRUA));
    }

    private static List<List<DirectionalRoute>> sequencesLUA() {
        return List.of(
                List.of(LDLA, RUA, RA),
                List.of(DLLA, RUA, RA));
    }

    private static List<List<DirectionalRoute>> sequencesURA() {
        return List.of(
                List.of(LA, DRA, UA),
                List.of(LA, RDA, UA));
    }

    private static List<List<DirectionalRoute>> sequencesRUA() {
        return List.of(
                List.of(DA, ULA, RA),
                List.of(DA, LUA, RA));
    }

    private static List<List<DirectionalRoute>> sequencesDLA() {
        return List.of(
                List.of(DLA, LA, RURA),
                List.of(DLA, LA, RRUA),
                List.of(LDA, LA, RURA),
                List.of(LDA, LA, RRUA));
    }

    private static List<List<DirectionalRoute>> sequencesLDA() {
        return List.of(
                List.of(DLLA, RA, URA),
                List.of(DLLA, RA, RUA),
                List.of(LDLA, RA, URA),
                List.of(LDLA, RA, RUA));
    }

    private static List<List<DirectionalRoute>> sequencesDRA() {
        return List.of(
                List.of(DLA, RA, UA),
                List.of(LDA, RA, UA));
    }

    private static List<List<DirectionalRoute>> sequencesRDA() {
        return List.of(
                List.of(DA, LA, URA),
                List.of(DA, LA, RUA));
    }

    private static List<List<DirectionalRoute>> sequencesRURA() {
        return List.of(
                List.of(DA, ULA, DRA, UA),
                List.of(DA, ULA, RDA, UA),
                List.of(DA, LUA, DRA, UA),
                List.of(DA, LUA, RDA, UA));
    }

    private static List<List<DirectionalRoute>> sequencesRRUA() {
        return List.of(
                List.of(DA, A, ULA, RA),
                List.of(DA, A, LUA, RA));
    }

    private static List<List<DirectionalRoute>> sequencesDLLA() {
        return List.of(
                List.of(DLA, LA, A, RURA),
                List.of(DLA, LA, A, RRUA),
                List.of(LDA, LA, A, RURA),
                List.of(LDA, LA, A, RRUA));
    }

    private static List<List<DirectionalRoute>> sequencesLDLA() {
        return List.of(
                List.of(DLLA, RA, LA, RURA),
                List.of(DLLA, RA, LA, RRUA),
                List.of(LDLA, RA, LA, RURA),
                List.of(LDLA, RA, LA, RRUA));
    }

}
