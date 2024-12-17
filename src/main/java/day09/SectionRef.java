package day09;

public class SectionRef {

    private DiskSection left;
    private DiskSection right;

    public SectionRef(DiskSection lone) {
        this.left = lone;
    }

    public SectionRef(DiskSection left, DiskSection right) {
        this.left = left;
        this.right = right;
    }

    public void setSections(DiskSection lone) {
        this.left = lone;
        this.right = null;
    }

    public void setSections(DiskSection left, DiskSection right) {
        this.left = left;
        this.right = right;
    }

    public boolean isPair() {
        return this.right != null;
    }

    public DiskSection get() {
        return getLeft();
    }

    public DiskSection getLeft() {
        return this.left;
    }

    public DiskSection getRight() {
        return this.right;
    }

    @Override
    public String toString() {
        if (this.right != null) {
            return "{ " + left + " | " + right + " }";
        } else {
            return "{ " + left + " }";
        }
    }
}