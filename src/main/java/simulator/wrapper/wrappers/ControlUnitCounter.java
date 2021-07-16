package simulator.wrapper.wrappers;

import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class ControlUnitCounter extends Wrapper {
    public ControlUnitCounter(String label, Link... links) {
        super(label, "1X6", links);
    }

    @Override
    public void initialize() {

    }
}

