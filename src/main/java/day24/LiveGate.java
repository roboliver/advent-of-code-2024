package day24;

import java.util.function.BiFunction;

public class LiveGate {

    private final BiFunction<Boolean, Boolean, Boolean> operator;
    private Boolean firstInputWire;
    private Boolean secondInputWire;
    private final String outputWire;

    public LiveGate(String operatorStr, String outputWire) {
        this.operator = switch (operatorStr) {
            case "AND" -> Boolean::logicalAnd;
            case "OR" -> Boolean::logicalOr;
            case "XOR" -> Boolean::logicalXor;
            default -> throw new IllegalStateException();
        };
        this.outputWire = outputWire;
    }

    public String getOutputWire() {
        return outputWire;
    }

    public void setInput(boolean input) {
        if (this.firstInputWire == null) {
            this.firstInputWire = input;
        } else if (this.secondInputWire == null) {
            this.secondInputWire = input;
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isReady() {
        return this.firstInputWire != null && this.secondInputWire != null;
    }

    public boolean run() {
        return operator.apply(this.firstInputWire, this.secondInputWire);
    }
}
