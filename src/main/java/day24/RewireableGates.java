package day24;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RewireableGates {

    private final Map<String, List<RewireableGate>> gatesByInput;
    private final Map<String, RewireableGate> gatesByOutput;

    public RewireableGates(List<Gate> gates) {
        var gatesByInput = new HashMap<String, List<RewireableGate>>();
        var gatesByOutput = new HashMap<String, RewireableGate>();
        for (var gate : gates) {
            var rewireableGate = new RewireableGate(gate);
            var leftGates = gatesByInput.computeIfAbsent(gate.leftInput(), it -> new ArrayList<>());
            var rightGates = gatesByInput.computeIfAbsent(gate.rightInput(), it -> new ArrayList<>());
            leftGates.add(rewireableGate);
            rightGates.add(rewireableGate);
            gatesByOutput.put(gate.output(), rewireableGate);
        }
        this.gatesByInput = Collections.unmodifiableMap(gatesByInput);
        this.gatesByOutput = gatesByOutput;
    }

    public String fixSwappedOutputs() {
        var swapped = new ArrayList<String>();
        fixLowestBit(swapped);
        var bitFromLower = findGate("x00", "y00", Operator.AND)
                .orElseThrow();
        for (int i = 1; i < 45; i++) {
            fixBit(i, bitFromLower, swapped);
            bitFromLower = fixLowerBitForNext(i, (i == 44), swapped);
        }
        return swapped.stream()
                .sorted()
                .collect(Collectors.joining(","));
    }

    private void fixLowestBit(List<String> swapped) {
        // lowest bit is simple -- should just be x00 XOR y00 -> z00.
        var gate = findGate("x00", "y00", Operator.XOR)
                .orElseThrow();
        if (!gate.getOutput().equals("z00")) {
            swapOutputs(gate, "z00", swapped);
        }
    }

    private void fixBit(int i, RewireableGate bitFromLower, List<String> swapped) {
        // fix the inputs for an output bit (z), based on the same-position input (x and y) bits, plus the gate for that
        // bit as produced by lower-position bits.
        //
        // some assumptions are made re: which outputs can be wrong, as detailed below.

        // get XOR of current-level x-input
        var xorX = findGate(fmtX(i), Operator.XOR)
                .orElseThrow();
        if (hasInput(xorX, fmtY(i))) {
            // x-input is XORed with y-input, so the output bit (z) will be the XOR of this (x-XOR-y) and the
            // lower-level bit
            fixBitParallel(i, bitFromLower, xorX, swapped);
        } else {
            // x and y aren't XORed together, which means one must be XORed with lower-bit, and the output of that is
            // XORed with the other to output z.
            fixBitSerial(i, bitFromLower, xorX, swapped);
        }
    }

    private void fixBitParallel(int i, RewireableGate bitFromLower, RewireableGate xorXy, List<String> swapped) {
        // in this branch we assume that possible errors are:
        //
        // - outputs of one or both of lower-bit and current x-XOR-y, OR
        // - output of XOR of the above two (i.e. which should go to current-level z).
        //
        // if the latter's output AND at least one of the former's outputs are BOTH wrong, we won't be able to resolve
        // the correct configuration.
        var xorLowerXyOptional = findGate(bitFromLower.getOutput(), xorXy.getOutput(), Operator.XOR);
        if (xorLowerXyOptional.isPresent()) {
            // found XOR of lower-bit and x-XOR-y -- should lead to current z
            var xorLowerXy = xorLowerXyOptional.get();
            if (!xorLowerXy.getOutput().equals(fmtZ(i))) {
                // doesn't lead to current z, so fix this
                swapOutputs(xorLowerXy, fmtZ(i), swapped);
            }
        } else {
            // XOR of lower-bit and x-XOR-y is missing, so output of one or both of lower-bit and x-XOR-y must be
            // wrong
            //
            // find which one's output leads to z -- the other's output must be wrong
            var xorLowerToZOptional = findGate(bitFromLower.getOutput(), Operator.XOR, fmtZ(i));
            var xorXyToZOptional = findGate(xorXy.getOutput(), Operator.XOR, fmtZ(i));
            if (xorLowerToZOptional.isPresent() && xorXyToZOptional.isPresent()) {
                // both lower-bit and x-XOR-y shouldn't be XORed to z, given our assumptions...
                throw new IllegalStateException();
            } else if (xorLowerToZOptional.isPresent()) {
                // lower-bit XORs to z, x-XOR-y doesn't, so fix output of x-XOR-y.
                swapOutputs(xorXy, getOtherInput(xorLowerToZOptional.get(), bitFromLower.getOutput()), swapped);
            } else if (xorXyToZOptional.isPresent()) {
                // x-XOR-y XORs to z, lower-bit doesn't, so fix output of lower-bit.
                swapOutputs(bitFromLower, getOtherInput(xorXyToZOptional.get(), xorXy.getOutput()), swapped);
            } else {
                // lower-bit and x-XOR-y aren't XORed together, and neither XORs to z, so the outputs of both
                // lower-bit and x-XOR-y must be wrong -- switch them with the inputs of the XOR to z.
                var xorToZ = gatesByOutput.get(fmtZ(i));
                swapOutputs(bitFromLower, xorToZ.getInputLeft(), swapped);
                swapOutputs(xorXy, xorToZ.getInputRight(), swapped);
            }
        }
    }

    private void fixBitSerial(int i, RewireableGate bitFromLower, RewireableGate xorX, List<String> swapped) {
        // in this branch, we can assume up to two of these outputs (i.e.: lower-bit; lower-bit XOR one of x-or-y;
        // and the output of that XORed with the other of x-or-y) are wrong.
        var xorY = findGate(fmtY(i), Operator.XOR)
                .orElseThrow();
        if (hasInput(xorY, xorX.getOutput()) ||
                hasInput(xorX, bitFromLower.getOutput()) ||
                xorY.getOutput().equals(fmtZ(i))) {
            // order must be: lower-bit-XOR-x -> XOR-y -> z, because we found one of these links.
            if (!hasInput(xorX, bitFromLower.getOutput())) {
                // missing lower-bit as input to x-XOR, so fix that
                swapOutputs(bitFromLower, getOtherInput(xorX, fmtX(i)), swapped);
            }
            if (!hasInput(xorY, xorX.getOutput())) {
                // missing x-XOR-lower-bit as input to XOR-y, so fix that
                swapOutputs(xorX, getOtherInput(xorY, fmtY(i)), swapped);
            }
            if (!xorY.getOutput().equals(fmtZ(i))) {
                // x-XOR-y does not output z, so fix that
                swapOutputs(xorY, fmtZ(i), swapped);
            }
        } else if (hasInput(xorX, xorY.getOutput()) ||
                hasInput(xorY, bitFromLower.getOutput()) ||
                xorX.getOutput().equals(fmtZ(i))) {
            // order must be: lower-bit-XOR-y -> XOR-x -> z, because we found one of these links.
            if (!hasInput(xorY, bitFromLower.getOutput())) {
                // missing lower-bit as input to y-XOR, so fix that
                swapOutputs(bitFromLower, getOtherInput(xorY, fmtY(i)), swapped);
            }
            if (!hasInput(xorX, xorY.getOutput())) {
                // missing y-XOR-lower-bit as input to XOR-x, so fix that
                swapOutputs(xorY, getOtherInput(xorX, fmtX(i)), swapped);
            }
            if (!xorX.getOutput().equals(fmtZ(i))) {
                // x-XOR-y does not output z, so fix that
                swapOutputs(xorX, fmtZ(i), swapped);
            }
        } else {
            // no links found at all to indicate the correct order, so our assumptions failed
            throw new IllegalStateException();
        }
    }

    private RewireableGate fixLowerBitForNext(int i, boolean isLastIteration, List<String> swapped) {
        // find the lower-bit that will be used in calculating the next bit (i.e. for i+1).
        //
        // if its output is wrong, no problem, we can fix that later; but if it isn't comprised of the expected two
        // inputs, we will fix that in this function.
        //
        // we assume that these two inputs can't have been both switched with inputs to the same, wrong, OR/XOR gate
        // -- in other words, we assume that one of them does lead to the lower-bit gate, even if the other doesn't.
        var xorToZ = gatesByOutput.get(fmtZ(i));
        // the inputs we're looking for will be the AND equivalents of the two inputs to z. first, get the one that goes
        // to z directly.
        RewireableGate nextLowerBitFirstInput = findGate(xorToZ.getInputLeft(), xorToZ.getInputRight(), Operator.AND)
                .orElseThrow();
        RewireableGate nextLowerBitSecondInput;
        if (hasInput(xorToZ, fmtX(i))) {
            // the current-level z had x as a direct input, so the inputs to the next lower-bit will be
            // current-lower-bit-AND-y, and the XORed version of those inputs ANDed with x.
            var xorLowerY = gatesByOutput.get(getOtherInput(xorToZ, fmtX(i)));
            nextLowerBitSecondInput = findGate(xorLowerY.getInputLeft(), xorLowerY.getInputRight(), Operator.AND)
                    .orElseThrow();
        } else if (hasInput(xorToZ, fmtY(i))) {
            // the current-level z had x as a direct input, so the inputs to the next lower-bit will be
            // current-lower-bit-AND-y, and the XORed version of those inputs ANDed with x.
            var xorLowerX = gatesByOutput.get(getOtherInput(xorToZ, fmtY(i)));
            nextLowerBitSecondInput = findGate(xorLowerX.getInputLeft(), xorLowerX.getInputRight(), Operator.AND)
                    .orElseThrow();
        } else {
            // the current-level z had neither x nor y as direct inputs -- instead, it was produced from the current
            // lower-bit XORed with x-XOR-y. so, we can just find the missing gate via the AND of x and y directly.
            nextLowerBitSecondInput = findGate(fmtX(i), fmtY(i), Operator.AND)
                    .orElseThrow();
        }
        // the next lower bit can technically be either ORed or XORed, although it seems in practice it's always ORed.
        var nextLowerBitOptional = findGate(nextLowerBitFirstInput.getOutput(), nextLowerBitSecondInput.getOutput(),
                Set.of(Operator.XOR, Operator.OR));
        if (nextLowerBitOptional.isPresent()) {
            return nextLowerBitOptional.get();
        } else if (!isLastIteration) {
            // on the last iteration, the next lower-bit will just be the highest z bit, so there's no correction needed
            // at this stage. for earlier iterations, we will fix the output of whichever next-lower-bit-input doesn't
            // actually go into the lower bit gate.
            var lowerBitFromFirstOptional = findGate(nextLowerBitFirstInput.getOutput(), Set.of(Operator.XOR, Operator.OR));
            var lowerBitFromSecondOptional = findGate(nextLowerBitSecondInput.getOutput(), Set.of(Operator.XOR, Operator.OR));
            if (lowerBitFromFirstOptional.isPresent() && isNextLowerBit(i, lowerBitFromFirstOptional.get())) {
                // first input to next-lower-bit leads to the correct gate, so switch the other input to this gate with
                // the output of the second gate that should input to it.
                var lowerBitFromFirst = lowerBitFromFirstOptional.get();
                var otherInput = gatesByOutput.get(getOtherInput(lowerBitFromFirst, nextLowerBitFirstInput.getOutput()));
                swapOutputs(otherInput, nextLowerBitSecondInput.getOutput(), swapped);
                return lowerBitFromFirst;
            } else if (lowerBitFromSecondOptional.isPresent() && isNextLowerBit(i, lowerBitFromSecondOptional.get())) {
                // second input to next-lower-bit leads to the correct gate, so switch the other input to this gate with
                // the output of the first gate that should input to it.
                var lowerBitFromSecond = lowerBitFromSecondOptional.get();
                var otherInput = gatesByOutput.get(getOtherInput(lowerBitFromSecond, nextLowerBitSecondInput.getOutput()));
                swapOutputs(otherInput, nextLowerBitFirstInput.getOutput(), swapped);
                return lowerBitFromSecond;
            } else {
                // neither next-lower-bit-input leads into the next-lower-bit gate, so our assumptions didn't hold
                throw new IllegalStateException();
            }
        } else {
            // this can't happen, as in the last iteration, any necessary swaps will already have been made.
            throw new IllegalStateException();
        }
    }

    private boolean isNextLowerBit(int i, RewireableGate nextLowerBitCandidate) {
        // find the next-lower-bit based on it:
        //         - outputting to the next z, OR
        return Optional.of(gatesByOutput.get(fmtZ(i + 1)))
                .filter(it -> hasInput(it, nextLowerBitCandidate.getOutput()))
                // - being XORed with the next x, OR
                .or(() -> findGate(fmtX(i + 1), nextLowerBitCandidate.getOutput(), Operator.XOR))
                // - being XORed with the next y, OR
                .or(() -> findGate(fmtY(i + 1), nextLowerBitCandidate.getOutput(), Operator.XOR))
                // - being XORed with the XOR of the next X and Y.
                .or(() -> findGate(fmtX(i + 1), fmtY(i + 1), Operator.XOR)
                        .flatMap(it -> findGate(it.getOutput(), nextLowerBitCandidate.getOutput(), Operator.XOR)))
                .isPresent();
    }

    private Optional<RewireableGate> findGate(String input, Operator operator) {
        return findGateImpl(input, it -> true, it -> it == operator, it -> true);
    }

    private Optional<RewireableGate> findGate(String firstInput, String secondInput, Operator operator) {
        return findGateImpl(firstInput, it -> hasInput(it, secondInput), it -> it == operator, it -> true);
    }

    private Optional<RewireableGate> findGate(String firstInput, Set<Operator> operators) {
        return findGateImpl(firstInput, it -> true, operators::contains, it -> true);
    }

    private Optional<RewireableGate> findGate(String firstInput, String secondInput, Set<Operator> operators) {
        return findGateImpl(firstInput, it -> hasInput(it, secondInput), operators::contains, it -> true);
    }

    private Optional<RewireableGate> findGate(String input, Operator operator, String output) {
        return findGateImpl(input, it -> true, it -> it == operator, it -> it.equals(output));
    }

    private Optional<RewireableGate> findGateImpl(String firstInput, Predicate<RewireableGate> secondInputFilter,
                                                  Predicate<Operator> operatorFilter, Predicate<String> outputFilter) {
        return gatesByInput.get(firstInput)
                .stream()
                .filter(secondInputFilter)
                .filter(it -> operatorFilter.test(it.getOperator()))
                .filter(it -> outputFilter.test(it.getOutput()))
                .findFirst();
    }

    private boolean hasInput(RewireableGate gate, String input) {
        return gate.getInputLeft().equals(input) || gate.getInputRight().equals(input);
    }

    private String getOtherInput(RewireableGate gate, String input) {
        if (gate.getInputRight().equals(input)) {
            return gate.getInputLeft();
        } else if (gate.getInputLeft().equals(input)) {
            return gate.getInputRight();
        } else {
            throw new IllegalStateException("something went wrong, didn't have expected input");
        }
    }

    private void swapOutputs(RewireableGate first, String secondOutput, List<String> swapped) {
        var firstOutput = first.getOutput();
        var second = gatesByOutput.get(secondOutput);
        first.setOutput(secondOutput);
        second.setOutput(firstOutput);
        gatesByOutput.remove(firstOutput);
        gatesByOutput.remove(secondOutput);
        gatesByOutput.put(first.getOutput(), first);
        gatesByOutput.put(second.getOutput(), second);
        swapped.add(firstOutput);
        swapped.add(secondOutput);
    }

    private String fmtX(int num) {
        return fmt("x", num);
    }

    private String fmtY(int num) {
        return fmt("y", num);
    }

    private String fmtZ(int num) {
        return fmt("z", num);
    }

    private String fmt(String letter, int num) {
        return letter + String.format("%02d", num);
    }
}
