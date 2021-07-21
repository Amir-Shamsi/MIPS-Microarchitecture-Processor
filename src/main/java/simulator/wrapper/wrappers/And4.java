package simulator.wrapper.wrappers;

import simulator.gates.combinational.And;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class And4 extends Wrapper {
    public And4(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        And and0 = new And("And_AND0", getInput(0), getInput(1));
        And and1 = new And("And_AND1", getInput(2), getInput(3));
        And and2 = new And("And_AND2", and1.getInput(0), and0.getInput(0));

        setOutput(0, and2.getOutput(0));
    }
}
