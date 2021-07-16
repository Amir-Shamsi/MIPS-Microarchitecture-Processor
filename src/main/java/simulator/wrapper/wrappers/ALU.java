package simulator.wrapper.wrappers;

import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class ALU extends Wrapper {
    public ALU(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {

    }
}
