package simulator.wrapper.wrappers;

import simulator.control.Simulator;
import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
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


        Link[] firstResult = new Link[32];
        for (int index = 0; index < 32; index++){
            And and0 = new And("MUX2X1_F_AND" + index, not.getInput(0), getInput(1 + index));
            firstResult[index] = and0.getOutput(0);
        }

        Link[] secondResult = new Link[32];
        for (int index = 0; index < 32; index++){
            And and1 = new And("MUX2X1_S_AND" + index, not.getInput(0), getInput(1 + index));
            secondResult[index] = and1.getOutput(0);
        }

        // setting result
        for (int index = 0; index < 32; index++){
            Or or = new Or("MUX2X1_OR" + index, firstResult[index], secondResult[index]);
            setOutput(index, or.getOutput(0));
        }
    }
}
