package day17;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Computer {

    private int ip = 0;
    private long regA;
    private long regB;
    private long regC;
    private final Operation[] program;
    private final List<Integer> output = new ArrayList<>();

    public Computer(long regA, long regB, long regC, List<Operation> program) {
        this.regA = regA;
        this.regB = regB;
        this.regC = regC;
        this.program = program.toArray(new Operation[0]);
    }

    public void runProgram() {
        while (ip < program.length) {
            var operation = program[ip];
            boolean movedIp = operation.execute(this);
            if (!movedIp) {
                ip++;
            }
        }
    }

    public long getA() {
        return this.regA;
    }

    public void setA(long regA) {
        this.regA = regA;
    }

    public long getB() {
        return this.regB;
    }

    public void setB(long regB) {
        this.regB = regB;
    }

    public long getC() {
        return this.regC;
    }

    public void setC(long regC) {
        this.regC = regC;
    }

    public long getComboOperand(int operand) {
        return switch (operand) {
            case 4 -> this.regA;
            case 5 -> this.regB;
            case 6 -> this.regC;
            default -> operand;
        };
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public void appendOutput(int i) {
        this.output.add(i);
    }

    public String getOutputStr() {
        return output.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public int[] getOutputArray() {
        return output.stream()
                .mapToInt(it -> it)
                .toArray();
    }
}
