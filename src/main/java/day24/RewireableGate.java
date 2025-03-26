package day24;

public class RewireableGate {
    private final String inputLeft;
    private final String inputRight;
    private final Operator operator;
    private String output;

    public RewireableGate(Gate gate) {
        this.inputLeft = gate.leftInput();
        this.inputRight = gate.rightInput();
        this.operator = Operator.fromString(gate.operator());
        this.output = gate.output();
    }

    public String getInputLeft() {
        return this.inputLeft;
    }

    public String getInputRight() {
        return this.inputRight;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public String getOutput() {
        return this.output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

}
