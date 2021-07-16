package simulator.wrapper.wrappers;

import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.DFlipFlop;

public class ProgramCounter extends Wrapper {
    public ProgramCounter(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        // wait for edge of clock to set new 'PC'
        DFlipFlop[] dFlipFlops = new DFlipFlop[32];

        for (int index = 0; index < inputSize; index++) {
            dFlipFlops[index] = new DFlipFlop(
                    "PROGRAM_COUNTER_DF_" + index,
                    "2X2",
                    getInput(0), //set Clock
                    getInput(index + 1)); //set Data
        }

        for (int index = 0; index < outputSize; index++)
            addOutput(dFlipFlops[index].getOutput(0));
    }

}