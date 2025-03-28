package day22;

import config.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class DayTwentyTwo implements Day<Long> {

    private static final int NEW_NUMBERS_PER_DAY = 2000;

    private final List<Long> initialSecrets;

    public DayTwentyTwo(BufferedReader input) throws IOException {
        try (input) {
            this.initialSecrets = input.lines()
                    .map(Long::parseLong)
                    .toList();
        }
    }

    @Override
    public String partOneName() {
        return "2000th secret number sum";
    }

    @Override
    public Long partOne() {
        return initialSecrets.stream()
                .mapToLong(it -> it)
                .map(this::evolveForDay)
                .sum();
    }

    @Override
    public String partTwoName() {
        return "maximum bananas attainable";
    }

    @Override
    public Long partTwo() {
        // 19-element 4D arrays to cover each possible sequence value -9 to 9, with
        // one dimension per price change. Faster than a HashMap!
        var bananasBySequence = new long[19][19][19][19];
        var seenSequences = new int[19][19][19][19];
        long maxBananas = 0L;
        for (int buyer = 0; buyer < initialSecrets.size(); buyer++) {
            long secret = initialSecrets.get(buyer);
            var sequenceFrame = new SequenceFrame();
            sequenceFrame.addLatestSecret(secret);
            for (int i = 0; i < NEW_NUMBERS_PER_DAY; i++) {
                secret = evolve(secret);
                sequenceFrame.addLatestSecret(secret);
                if (i > 3) {
                    var sequence = sequenceFrame.toSequence();
                    if (!seenSequence(seenSequences, sequence, buyer)) {
                        markSeen(seenSequences, sequence, buyer);
                        long bananas = secretNumberToBananas(secret);
                        addBananas(bananasBySequence, sequence, bananas);
                        maxBananas = Math.max(maxBananas, getBananas(bananasBySequence, sequence));
                    }
                }
            }
        }
        return maxBananas;
    }

    private long evolveForDay(long initialSecret) {
        long secret = initialSecret;
        for (int i = 0; i < NEW_NUMBERS_PER_DAY; i++) {
            secret = evolve(secret);
        }
        return secret;
    }

    private long evolve(long secret) {
        secret = mixAndPrune(secret, secret * 64);
        secret = mixAndPrune(secret, secret / 32);
        secret = mixAndPrune(secret, secret * 2048);
        return secret;
    }

    private long mixAndPrune(long secret, long mixer) {
        secret = secret ^ mixer;
        secret = secret % 16777216L;
        return secret;
    }

    private boolean seenSequence(int[][][][] seenSequences, int[] sequence, int buyer) {
        return seenSequences[seqIndex(sequence[0])]
                [seqIndex(sequence[1])]
                [seqIndex(sequence[2])]
                [seqIndex(sequence[3])] == buyerId(buyer);
    }

    private void markSeen(int[][][][] seenSequences, int[] sequence, int buyer) {
        seenSequences[seqIndex(sequence[0])]
                [seqIndex(sequence[1])]
                [seqIndex(sequence[2])]
                [seqIndex(sequence[3])] = buyerId(buyer);
    }

    private int buyerId(int buyer) {
        // needed because the values in the seenSequences array will initialise to 0
        return buyer + 1;
    }

    private long getBananas(long[][][][] bananasBySequence, int[] sequence) {
        return bananasBySequence[seqIndex(sequence[0])]
                [seqIndex(sequence[1])]
                [seqIndex(sequence[2])]
                [seqIndex(sequence[3])];
    }

    private void addBananas(long[][][][] bananasBySequence, int[] sequence, long bananas) {
        bananasBySequence[seqIndex(sequence[0])]
                [seqIndex(sequence[1])]
                [seqIndex(sequence[2])]
                [seqIndex(sequence[3])] += bananas;
    }

    private int seqIndex(int priceChange) {
        // possible price change values are -9 to 9, so -9 is indexed at 0, -8 at 1, etc.
        return priceChange + 9;
    }

    static int secretNumberToBananas(long secret) {
        return (int) (secret % 10L);
    }

}
