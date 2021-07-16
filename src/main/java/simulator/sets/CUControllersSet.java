package simulator.sets;

import simulator.control.Simulator;
import simulator.network.Link;

public class CUControllersSet {
    public static Link RegDst;
    public static Link ALUSrc;
    public static Link MemToReg;
    public static Link RegWrite;
    public static Link MemRead;
    public static Link MemWrite;
    public static Link Branch;
    public static Link ALUOp0;
    public static Link ALUOp1;

    static {
        RegDst = Simulator.falseLogic;
        ALUSrc = Simulator.falseLogic;
        MemToReg = Simulator.falseLogic;
        RegWrite = Simulator.falseLogic;
        MemRead = Simulator.falseLogic;
        MemWrite = Simulator.falseLogic;
        Branch = Simulator.falseLogic;
        ALUOp0 = Simulator.falseLogic;
        ALUOp1 = Simulator.falseLogic;
    }
}

