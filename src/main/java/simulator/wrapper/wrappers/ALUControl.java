package simulator.wrapper.wrappers;
import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.sets.CUControllersSet;
import simulator.sets.InstructionSet;
import simulator.wrapper.Wrapper;

public class ALUControl extends Wrapper {
    public ALUControl(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        Not not0 = new Not("ALU_CONTROL_NOT0", CUControllersSet.ALUOp0);
        And and0 = new And("ALU_CONTROL_AND0", not0.getOutput(0), CUControllersSet.ALUOp0);
        setOutput(3, and0.getOutput(0));

        And and1 = new And("ALU_CONTROL_AND1", CUControllersSet.ALUOp1, InstructionSet.func[1]);
        Or or0 = new Or("ALU_CONTROL_OR0", and1.getOutput(0), CUControllersSet.ALUOp0);
        setOutput(2, or0.getOutput(0));

        Not not1 = new Not("ALU_CONTROL_NOT1", CUControllersSet.ALUOp1);
        Not not2 = new Not("ALU_CONTROL_NOT2", InstructionSet.func[2]);
        Or or1 = new Or("ALU_CONTROL_OR1", not1.getOutput(0), not2.getOutput(0));
        setOutput(1, or1.getOutput(0));

        Or or2 = new Or("ALU_CONTROL_OR2", InstructionSet.func[0], InstructionSet.func[3]);
        And and2 = new And("ALU_CONTROL_AND2", or2.getOutput(0), CUControllersSet.ALUOp1);
        setOutput(0, and2.getOutput(0));



    }
}
