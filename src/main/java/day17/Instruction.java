package day17;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum Instruction {
    ADV(0, Instruction::advExec),
    BXL(1, Instruction::bxlExec),
    BST(2, Instruction::bstExec),
    JNZ(3, Instruction::jnzExec),
    BXC(4, Instruction::bxcExec),
    OUT(5, Instruction::outExec),
    BDV(6, Instruction::bdvExec),
    CDV(7, Instruction::cdvExec);

    private final int opcode;
    private final BiFunction<Computer, Integer, Boolean> execFunc;

    Instruction(int opcode, BiFunction<Computer, Integer, Boolean> execFunc) {
        this.opcode = opcode;
        this.execFunc = execFunc;
    }

    public boolean execute(Computer computer, int operand) {
        return this.execFunc.apply(computer, operand);
    }

    public static Instruction parse(int opcode) {
        return Arrays.stream(values())
                .filter(it -> it.opcode == opcode)
                .findFirst()
                .orElseThrow();
    }

    public int getOpcode() {
        return this.opcode;
    }

    private static boolean advExec(Computer computer, int operand) {
        long res = computer.getA() >> computer.getComboOperand(operand);
        computer.setA(res);
        return false;
    }

    private static boolean bxlExec(Computer computer, int operand) {
        long res = computer.getB() ^ operand;
        computer.setB(res);
        return false;
    }

    private static boolean bstExec(Computer computer, int operand) {
        long res = computer.getComboOperand(operand) % 8;
        computer.setB(res);
        return false;
    }

    private static boolean jnzExec(Computer computer, int operand) {
        if (computer.getA() == 0) {
            return false;
        }
        computer.setIp(operand / 2);
        return true;
    }

    private static boolean bxcExec(Computer computer, int operand) {
        long res = computer.getB() ^ computer.getC();
        computer.setB(res);
        return false;
    }

    private static boolean outExec(Computer computer, int operand) {
        int res = (int) (computer.getComboOperand(operand) % 8);
        computer.appendOutput(res);
        return false;
    }

    private static boolean bdvExec(Computer computer, int operand) {
        long res = computer.getA() >> computer.getComboOperand(operand);
        computer.setB(res);
        return false;
    }

    private static boolean cdvExec(Computer computer, int operand) {
        long res = computer.getA() >> computer.getComboOperand(operand);
        computer.setC(res);
        return false;
    }
}
