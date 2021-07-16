package simulator.wrapper.wrappers;

import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class ShiftLeftLogical extends Wrapper {
    public ShiftLeftLogical(String label, String stream, Link... links) {
        super(label, stream.split("<<")[0] + "X" +
                (Integer.parseInt(stream.split("<<")[0]) +
                        Integer.parseInt(stream.split("<<")[1])), links);
    }

    @Override
    public void initialize() {
        /*
        * inputSize = size of shift
        * outputSize = size of shifted immediate
        * */

        for(int index = 1; index <= inputSize; index++){
            setOutput(outputSize - index, Simulator.falseLogic);
        }
        for (int index = 0; index < outputSize - inputSize; index++)
            setOutput(index, getInput(index));
    }
}
