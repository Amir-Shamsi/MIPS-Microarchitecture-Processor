package simulator.wrapper.wrappers;

import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class SignExtension extends Wrapper {

    public SignExtension(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        for (int index = outputSize/2; index < outputSize; index++)
            setOutput(index, getInput(0));
        for (int index = 0; index < outputSize/2; index++)
            setOutput(index, getInput(index));
    }

}
