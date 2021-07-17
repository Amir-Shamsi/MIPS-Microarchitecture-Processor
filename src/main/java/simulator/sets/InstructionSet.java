package simulator.sets;

import simulator.network.Link;

public class InstructionSet {
    public static Link[]
            opCode,
            resSource,
            resTarget,
            immediate,
            targetAddress,
            resDestination,
            shiftAmount,
            func;
    static {
        opCode = new Link[6];
        resSource = new Link[5];
        resTarget = new Link[5];
        immediate = new Link[16];
        targetAddress = new Link[26];
        resDestination = new Link[5];
        shiftAmount = new Link[5];
        func = new Link[6];
    }
}
