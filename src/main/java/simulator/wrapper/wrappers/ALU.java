package simulator.wrapper.wrappers;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Nor;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.sets.ALUControllersSet;
import simulator.wrapper.Wrapper;

public class ALU extends Wrapper {
    public ALU(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {

        ALUCounter aluc = new ALUCounter("ALUC", getInput(0));

        //alu control
        //first two are read data1 and the mux(sign_extend, read_data2, alu_src)'s output
        Link[] data1 = new Link[32];
        for (int i=0;i<32;i++)
            data1[i]=getInput(i);

        Link[] data2 = new Link[32];
        for(int i=32;i<64;i++)
            data2[i-32] = getInput(i);


        Link[] control = new Link[4];
        for (int i=64;i<=64+4;i++)
            control[i-64]=getInput(i);

        //control = 0123
        //and = ~0.~2.~3
        //or = ~1.3
        //add = ~1.2
        //sub = ~0.1.~3
        //slt = 2.3
        //nor = 0
        Not not0 = new Not("ALUC_NOT0", control[0]);
        Not not1 = new Not("ALUC_NOT1", control[1]);
        Not not2 = new Not("ALUC_NOT2", control[2]);
        Not not3 = new Not("ALUC_NOT3", control[3]);

        //************************************** and **************************************
        ALUControllersSet.and=new And3("ALUC_AND~0_~2_~3","3X1",not0.getOutput(0),not2.getOutput(0),not3.getOutput(0)).getOutput(0);
        Link[] add_output = new Link[32];
        for (int i=0;i<32;i++){
            add_output[i]=new And("and_output"+i,data1[i],data2[i]).getOutput(0);
        }

        //************************************** or **************************************
        ALUControllersSet.or= new And("ALUC_AND~1_3",not1.getOutput(0),control[3]).getOutput(0);
        Link[] or_output = new Link[32];
        for (int i=0;i<32;i++){
            or_output[i]=new Or("or_output"+i,data1[i],data2[i]).getOutput(0);
        }

        //************************************** add **************************************
        ALUControllersSet.add= new And("ALUC_AND~1_2",not1.getOutput(0),control[2]).getOutput(0);

        //************************************** sub **************************************
        ALUControllersSet.sub= new And3("ALUC_AND~0_1_~3","3X1",not1.getOutput(0),control[1],not3.getOutput(0)).getOutput(0);

        //************************************** slt **************************************
        ALUControllersSet.slt= new And("ALUC_AND2_3",control[2],control[3]).getOutput(0);
        Link[] slt_output = new Link[32];
        int i;
        for (i=31;i>=0;i--){
            if ((data1[i].getSignal()) && (!data2[i].getSignal())){
                for (int j=0;j<31;j++)
                    slt_output[j].setSignal(false);
                break;
            }
            if ((!data1[i].getSignal()) && (data2[i].getSignal())){
                for (int j=0;j<31;j++)
                    slt_output[j].setSignal(true);
                break;
            }
        }
        if (i<=0){
            for (int j=0;j<31;j++)
                slt_output[j].setSignal(false);
        }

        //************************************** nor **************************************
        ALUControllersSet.nor= control[0];
        Link[] nor_output = new Link[32];
        for (int i1=0;i<32;i1++){
            nor_output[i1]=new Nor("and_output"+i,data1[i1],data2[i1]).getOutput(0);
        }


    }
}
