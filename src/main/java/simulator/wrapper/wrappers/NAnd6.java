package simulator.wrapper.wrappers;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class NAnd6 extends Wrapper {
    public NAnd6(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        Not not0 = new Not("NAnd_NOT0", getInput(0));
        Not not1 = new Not("NAnd_NOT1", getInput(1));
        Not not2 = new Not("NAnd_NOT2", getInput(2));
        Not not3 = new Not("NAnd_NOT3", getInput(3));
        Not not4 = new Not("NAnd_NOT4", getInput(4));
        Not not5 = new Not("NAnd_NOT5", getInput(5));

        And and0 = new And("NAnd_AND0", not0.getOutput(0), not1.getOutput(0));
        And and1 = new And("NAnd_AND0", not2.getOutput(0), not3.getOutput(0));
        And and2 = new And("NAnd_AND0", not4.getOutput(0), not5.getOutput(0));

        And and3 = new And("NAnd_AND0", and0.getOutput(0), and1.getOutput(0));
        And and4 = new And("NAnd_AND0", and2.getOutput(0), and3.getOutput(0));

        setOutput(0, and4.getOutput(0));
    }
}
