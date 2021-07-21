package simulator.sets;

import simulator.control.Simulator;
import simulator.network.Link;

public class ALUControllersSet {
    public static Link and;
    public static Link or;
    public static Link add;
    public static Link sub;
    public static Link slt;
    public static Link nor;
    public static Link zero;
    public static Link result;
    public static Link carryOut;
    public static Link overflow;
    static {
        and = Simulator.falseLogic;
        or = Simulator.falseLogic;
        add = Simulator.falseLogic;
        sub = Simulator.falseLogic;
        slt = Simulator.falseLogic;
        nor = Simulator.falseLogic;
        zero = Simulator.falseLogic;
        result = Simulator.falseLogic;
        carryOut = Simulator.falseLogic;
        overflow = Simulator.falseLogic;
    }

}
