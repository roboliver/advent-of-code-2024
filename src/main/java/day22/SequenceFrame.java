package day22;

import static day22.DayTwentyTwo.secretNumberToBananas;

public class SequenceFrame {
    private final long[] secrets = new long[5];
    private int ptr = 0;

    public void addLatestSecret(long secretNum) {
        secrets[ptr] = secretNum;
        ptr = incPtr(ptr);
    }

    public int[] toSequence() {
        int[] sequence = new int[4];
        int ptrTmp = ptr;
        int older = secretNumberToBananas(secrets[ptrTmp]);
        for (int i = 0; i < 4; i++) {
            ptrTmp = incPtr(ptrTmp);
            int newer = secretNumberToBananas(secrets[ptrTmp]);
            sequence[i] = newer - older;
            older = newer;
        }
        return sequence;
    }

    private int incPtr(int ptr) {
        return (ptr + 1) % 5;
    }
}