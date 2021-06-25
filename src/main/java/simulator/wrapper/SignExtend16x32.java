package simulator.wrapper;

import simulator.network.Link;

public class SignExtend16x32 extends Wrapper {

    public SignExtend16x32(String label, Link... links) {
        super(label, "16X32", links);
    }

    @Override
    public void initialize() {
        for (int index = 0; index < outputSize/2; index++)
            setOutput(index, getInput(0));
        for (int index = outputSize/2; index < outputSize; index++)
            setOutput(index, getInput(index - outputSize/2));
    }
}
