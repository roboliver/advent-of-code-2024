package day17;

import common.DayDoubleType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class DaySeventeen implements DayDoubleType<String, Long> {

    private final long regA;
    private final long regB;
    private final long regC;
    private final List<Operation> program;

    public DaySeventeen(BufferedReader input) throws IOException {
        try (input) {
            this.regA = parseRegister(input.readLine());
            this.regB = parseRegister(input.readLine());
            this.regC = parseRegister(input.readLine());
            input.readLine();
            this.program = parseProgram(input.readLine());
        }
    }

    private long parseRegister(String line) {
        return Long.parseLong(line.split(": ")[1]);
    }

    private List<Operation> parseProgram(String line) {
        var program = line.split(": ")[1];
        var ints = Arrays.stream(program.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        var operations = new ArrayList<Operation>();
        for (int i = 0; i < ints.length / 2; i++) {
            int opcode = ints[i*2];
            int operand = ints[i*2 + 1];
            operations.add(new Operation(Instruction.parse(opcode), operand));
        }
        return operations;
    }

    @Override
    public String partOneName() {
        return "program output";
    }

    @Override
    public String partOne() {
        var computer = new Computer(regA, regB, regC, program);
        computer.runProgram();
        return computer.getOutputStr();
    }

    @Override
    public String partTwoName() {
        return "lowest A value that outputs the input program";
    }

    @Override
    public Long partTwo() {
        // Note: this function assumes that programs:
        // - have their last instruction as 3,0 (set IP to 0 unless A is 0)
        // - remove the last 3 bits from register A in each iteration
        // - make use of up to the last 10 bits of A in deciding the value to add to the output
        // - add exactly one value to the output each iteration
        // These assumptions are true for the test input, and for the main puzzle input I had,
        // but may not be for other possible valid Day 17 inputs.
        var programArray = unparseProgram();
        // initialise with numbers up to 10 bits
        var regAValues = initRegAValues(programArray);
        // maximum iterations: number of 3-bits that can be prepended to the initial 10-bit
        // regA values to fit in a long
        for (int i = 0; i < (53 / 3); i++) {
            var newRegAValues = new ArrayList<Long>();
            for (long a : regAValues) {
                for (int j = 0x0; j < 0x8; j++) {
                    long aNew = ((long) j << (10 + (3 * i))) | a; // prepend j to a
                    var output = runComputerWithRegA(aNew);
                    if (Arrays.equals(programArray, output)) {
                        // possible regA values are tried in order from 0 to LONG_MAX,
                        // so the first match will be the lowest
                        return aNew;
                    }
                    if (output.length > (i + 1) && output[i + 1] == programArray[i + 1]) {
                        // prepended 3-bits caused a new match, so retain this for next iteration
                        newRegAValues.add(aNew);
                    }
                }
            }
            regAValues = newRegAValues;
        }
        throw new IllegalStateException("no valid input found");
    }

    private List<Long> initRegAValues(int[] programArray) {
        var regAValues = new ArrayList<Long>();
        // initialise with all valid 10-bit numbers
        for (long a = 0x000; a < 0x400; a++) {
            var output = runComputerWithRegA(a);
            if (output[0] == programArray[0]) {
                regAValues.add(a);
            }
        }
        return regAValues;
    }

    private int[] runComputerWithRegA(long a) {
        var computer = new Computer(a, 0, 0, program);
        computer.runProgram();
        return computer.getOutputArray();
    }

    private int[] unparseProgram() {
        return program.stream()
                .flatMapToInt(it -> IntStream.of(it.instruction().getOpcode(), it.operand()))
                .toArray();
    }
}
