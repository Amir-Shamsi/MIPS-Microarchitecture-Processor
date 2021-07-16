package simulator.wrapper.wrappers;

import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.sets.CUControllersSet;
import simulator.wrapper.Wrapper;

public class ControlUnit extends Wrapper {

    public ControlUnit(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        ControlUnitCounter cuc = new ControlUnitCounter("CUC", getInput(0));

        Link[] opCode = new Link[6];
        for(int index = 0; index <= 6; index++)
            opCode[index] = getInput(index);

        //************************************** R-types **************************************
        NAnd6 nAnd0 = new NAnd6("CUC_NAnd", "6X6",
                opCode[0],
                opCode[1],
                opCode[2],
                opCode[3],
                opCode[4],
                opCode[5]);
        CUControllersSet.RegDst = nAnd0.getOutput(0);
        CUControllersSet.ALUOp1 = nAnd0.getOutput(0);


        //************************************** LW **************************************
        Not not0 = new Not("CUC_NOT0", opCode[1]);
        Not not1 = new Not("CUC_NOT1", opCode[2]);
        Not not2 = new Not("CUC_NOT2", opCode[3]);

        And6 and60 = new And6("CUC_AND6_0", "6X6",
                opCode[0],
                not0.getOutput(0),
                not1.getOutput(0),
                not2.getOutput(0),
                opCode[4],
                opCode[5]
        );
        CUControllersSet.MemRead = and60.getOutput(0);
        CUControllersSet.MemToReg = and60.getOutput(0);


        // ************************************** SW **************************************
        Not not3 = new Not("CUC_NOT3", opCode[1]);
        Not not4 = new Not("CUC_NOT4", opCode[3]);

        And6 and61 = new And6("CUC_AND6_1", "6X6",
                opCode[0],
                not3.getOutput(0),
                opCode[2],
                not4.getOutput(0),
                opCode[4],
                opCode[5]
        );
        CUControllersSet.MemWrite = and61.getOutput(0);


        //************************************** BEQ **************************************
        Not not5 = new Not("CUC_NOT5", opCode[3]);
        NAnd6 nAnd1 = new NAnd6("CUC_NAnd1", "6X6",
                opCode[0],
                opCode[1],
                opCode[2],
                not5.getOutput(0),
                opCode[4],
                opCode[5]);
        CUControllersSet.Branch = nAnd1.getOutput(0);
        CUControllersSet.ALUOp0 = nAnd1.getOutput(0);

        Or or0 = new Or("CUC_OR0", and60.getOutput(0), and61.getOutput(0));
        CUControllersSet.ALUSrc = or0.getOutput(0);

        Or or1 = new Or("CUC_OR1", and60.getOutput(0), nAnd0.getOutput(0));
        CUControllersSet.RegWrite = or1.getOutput(0);
    }
}
