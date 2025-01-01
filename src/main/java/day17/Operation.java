package day17;

public record Operation(Instruction instruction, int operand) {

    public boolean execute(Computer computer) {
        return instruction.execute(computer, operand);
    }

}
