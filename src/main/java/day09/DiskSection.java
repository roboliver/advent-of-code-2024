package day09;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static day09.DayNine.FREE_ID;

public class DiskSection {
    private DiskSection prev;
    private DiskSection next;

    private final int size;
    private final int id;

    public DiskSection(int size, int id) {
        this.size = size;
        this.id = id;
    }

    public void addAfter(DiskSection other) {
        if (other.prev != null || other.next != null) {
            throw new IllegalStateException("can only insert detached segments");
        }
        var oldNext = this.next;
        this.next = other;
        other.prev = this;
        if (oldNext != null) {
            oldNext.prev = other;
            other.next = oldNext;
        }
    }

    public void addBefore(DiskSection other) {
        this.prev.addAfter(other);
    }

    public void remove() {
        if (this.next != null) {
            this.next.prev = this.prev;
        }
        if (this.prev != null) {
            this.prev.next = this.next;
        }
    }

    public boolean canFill(int size, boolean truncate) {
        return isFree() && (size <= this.size || truncate);
    }

    public SectionRef fill(SectionRef fileRef, boolean truncate) {
        if (fileRef.isPair()) {
            throw new IllegalArgumentException("can only fill from lone file");
        }
        var file = fileRef.get();
        var fileSize = file.size;
        var fileId = file.id;
        if (!canFill(fileSize, truncate)) {
            throw new IllegalArgumentException("can't fill with size " + fileSize + " as block is only size " + this.size);
        }
        var emptiedSection = new DiskSection(Math.min(fileSize, this.size), FREE_ID);
        file.addAfter(emptiedSection);
        if (fileSize > this.size) {
            var fileResidual = new DiskSection(fileSize - this.size, fileId);
            file.addAfter(fileResidual);
            fileRef.setSections(fileResidual, emptiedSection);
        } else {
            fileRef.setSections(emptiedSection);
        }
        file.remove();

        SectionRef filledRef;
        var filledSection = new DiskSection(Math.min(fileSize, this.size), fileId);
        this.addBefore(filledSection);
        if (fileSize < this.size) {
            var emptyResidual = new DiskSection(this.size - fileSize, FREE_ID);
            this.addBefore(emptyResidual);
            filledRef = new SectionRef(filledSection, emptyResidual);
        } else {
            filledRef = new SectionRef(filledSection);
        }
        remove();
        return filledRef;
    }

    public boolean isFree() {
        return id == FREE_ID;
    }

    public boolean isFile() {
        return !isFree();
    }

    public DiskSection getPrev() {
        return this.prev;
    }

    public DiskSection getNext() {
        return this.next;
    }

    public int getId() {
        return this.id;
    }

    public int getSize() {
        return this.size;
    }

    public DiskSection getPrevFile() {
        return seekFor(DiskSection::isFile, DiskSection::getPrev);
    }

    public DiskSection getPrevFree() {
        return seekFor(DiskSection::isFree, DiskSection::getPrev);
    }

    public DiskSection getNextFile() {
        return seekFor(DiskSection::isFile, DiskSection::getNext);
    }

    public DiskSection getNextFree() {
        return seekFor(DiskSection::isFree, DiskSection::getNext);
    }

    private DiskSection seekFor(Function<DiskSection, Boolean> typeMatch, UnaryOperator<DiskSection> seekFunc) {
        var section = seekFunc.apply(this);
        while (section != null && !typeMatch.apply(section)) {
            section = seekFunc.apply(section);
        }
        return section;
    }

    @Override
    public String toString() {
        if (isFree()) {
            return "[FREE: size=" + size + "]";
        } else {
            return "[FILE: id=" + id + ", size=" + size + "]";
        }
    }
}