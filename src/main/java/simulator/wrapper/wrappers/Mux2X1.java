package simulator.wrapper.wrappers;

import simulator.control.Simulator;
import simulator.gates.combinational.Not;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Mux2X1 extends Wrapper {
    public Mux2X1(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        // a not logic over select
        Not not = new Not("MUX2X1_0", getInput(0));

        if (getInput(0) == Simulator.falseLogic) {
            for (int index = 0; index < 6; index++)
                setOutput(index, getInput(1 + index));
        }
        else{
            for (int index = 0; index < 6; index++)
                setOutput(index, getInput(7 + index));
        }
    }
}
