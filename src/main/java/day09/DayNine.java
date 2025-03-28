package day09;

import config.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class DayNine implements Day<Long> {

    private final String diskContents;

    public DayNine(BufferedReader input) throws IOException {
        try (input) {
            this.diskContents = input.readLine();
        }
    }

    @Override
    public String partOneName() {
        return "filesystem checksum";
    }

    @Override
    public Long partOne() {
        var diskContents = parseToArray();
        int start = 0;
        int end = diskContents.length - 1;
        while (start < end) {
            if (diskContents[end] == -1) {
                end--;
            } else if (diskContents[start] != -1) {
                start++;
            } else {
                diskContents[start] = diskContents[end];
                diskContents[end] = -1;
                end--;
                start++;
            }
        }
        return generateChecksum(diskContents);
    }

    @Override
    public Long partTwo() {
        var diskContents = parseToArray();
        int start = 0;
        int end = diskContents.length - 1;
        int fileId = -1;
        int fileSize = 0;
        while (start < end) {
            if (fileId == -1) {
                if (diskContents[end] == -1) {
                    end--;
                } else if (diskContents[start] != -1) {
                    start++;
                } else {
                    fileId = diskContents[end];
                }
            } else {
                if (fileId == diskContents[end]) {
                    fileSize++;
                    end--;
                } else {
                    int gapStart = findGap(fileSize, start, (end + 1), diskContents);
                    if (gapStart != -1) {
                        for (int i = 0; i < fileSize; i++) {
                            diskContents[gapStart + i] = diskContents[end + 1 + i];
                            diskContents[end + 1 + i] = -1;
                        }
                    }
                    fileId = -1;
                    fileSize = 0;
                }
            }
        }
        return generateChecksum(diskContents);
    }

    private int findGap(int fileSize, int start, int end, int[] diskContents) {
        int gapSize = 0;
        while (start < end) {
            if ((diskContents[start] == -1)) {
                gapSize++;
                if (gapSize >= fileSize) {
                    return start - (gapSize - 1);
                }
            } else {
                gapSize = 0;
            }
            start++;
        }
        return -1;
    }

    private long generateChecksum(int[] diskContents) {
        long checksum = 0L;
        int count = 0;
        for (int block : diskContents) {
            if (block != -1) {
                checksum += (long) count * block;
            }
            count++;
        }
        return checksum;
    }

    private int[] parseToArray() {
        var list = new ArrayList<Integer>();
        for (int i = 0; i < this.diskContents.length(); i++) {
            int size = Character.digit(this.diskContents.charAt(i), 10);
            for (int j = 0; j < size; j++) {
                list.add((i % 2 == 0) ? (i / 2) : -1);
            }
        }
        return list.stream()
                .mapToInt(it -> it)
                .toArray();
    }

    @Override
    public String partTwoName() {
        return "filesystem checksum without fragmentation";
    }

}
