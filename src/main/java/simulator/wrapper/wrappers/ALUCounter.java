package simulator.wrapper.wrappers;

import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class ALUCounter extends Wrapper {
    public ALUCounter(String label, Link... links) {
        super(label, "1X4", links);
    }
    @Override
    public void initialize(){

    }
}
