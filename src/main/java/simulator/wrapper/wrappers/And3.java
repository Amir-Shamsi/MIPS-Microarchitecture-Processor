package simulator.wrapper.wrappers;

import simulator.gates.combinational.And;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class And3 extends Wrapper {
    public And3(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        And and0 = new And("And_AND1", getInput(0), getInput(1));
        And and1 = new And("And_AND2", getInput(2), and0.getInput(0));

        setOutput(0, and1.getOutput(0));
    }
}
