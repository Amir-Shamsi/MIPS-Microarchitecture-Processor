package simulator.wrapper.wrappers;

import simulator.gates.combinational.And;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class And6 extends Wrapper {
    public And6(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        And and0 = new And("NAnd_AND0", getInput(0), getInput(1));
        And and1 = new And("NAnd_AND0", getInput(2), getInput(3));
        And and2 = new And("NAnd_AND0", getInput(4), getInput(5));

        And and3 = new And("NAnd_AND0", and0.getOutput(0), and1.getOutput(0));
        And and4 = new And("NAnd_AND0", and2.getOutput(0), and3.getOutput(0));

        setOutput(0, and4.getOutput(0));
    }
}
