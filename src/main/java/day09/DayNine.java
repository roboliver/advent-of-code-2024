package day09;

import common.Day;

import java.io.BufferedReader;
import java.io.IOException;

public class DayNine implements Day<Long> {

    static final int FREE_ID = -1;

    private final String diskContents;

    public DayNine(BufferedReader input) throws IOException {
        try (input) {
            this.diskContents = input.readLine();
        }
    }

    @Override
    public String partOneName() {
        return "";
    }

    @Override
    public Long partOne() {
        var ends = parseDiskContents();
        var firstFile = ends.getLeft();
        var firstFreeRef = new SectionRef(firstFile.getNext());
        var lastFileRef = new SectionRef(ends.getRight());
        while (firstFreeRef.get() != null) {
            var fillResult = firstFreeRef.get().fill(lastFileRef, true);
            if (lastFileRef.isPair()) {
                lastFileRef.getRight().remove();
                lastFileRef.setSections(lastFileRef.getLeft());
            } else {
                var lastFile = lastFileRef.get();
                while (lastFile.isFree()) {
                    var lastFileNew = lastFile.getPrev();
                    lastFile.remove();
                    lastFile = lastFileNew;
                }
                lastFileRef.setSections(lastFile);
            }
            if (fillResult.isPair()) {
                firstFreeRef.setSections(fillResult.getRight());
            } else {
                firstFreeRef.setSections(fillResult.get().getNextFree());
            }
        }
        return generateChecksum(firstFile);
    }

    private Long generateChecksum(DiskSection firstFile) {
        int i = 0;
        long checksum = 0L;
        while (firstFile != null) {
            for (int j = 0; j < firstFile.getSize(); j++) {
                checksum += (long) i * firstFile.getId();
                i++;
            }
            firstFile = firstFile.getNextFile();
        }
        return checksum;

        // 6432869891895
        // 6432340828820
    }

    private String generateString(DiskSection section) {
        var buf = new StringBuilder();
        while (section != null) {
            for (int i = 0; i < section.getSize(); i++) {
                if (section.getId() == FREE_ID) {
                    buf.append('.');
                } else {
                    buf.append(section.getId());
                }
            }
            section = section.getNext();
        }
        return buf.toString();
    }

    private SectionRef parseDiskContents() {
        var firstFile = parseSection(0);
        var curFile = firstFile;
        var lastFile = firstFile;
        for (int i = 1; i < diskContents.length(); i++) {
            var section = parseSection(i);
            curFile.addAfter(section);
            curFile = section;
            if (i % 2 == 0) {
                lastFile = section;
            }
        }
        return new SectionRef(firstFile, lastFile);
    }

    private DiskSection parseSection(int i) {
        int size = Character.digit(this.diskContents.charAt(i), 10);
        int id = (i % 2 == 0) ? i / 2 : FREE_ID;
        return new DiskSection(size, id);
    }

    @Override
    public String partTwoName() {
        return "";
    }

    @Override
    public Long partTwo() {
        return 0L;
    }

}
