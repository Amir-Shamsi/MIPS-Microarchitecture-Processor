package simulator;

import simulator.control.Simulator;
import simulator.gates.combinational.ByteMemory;
import simulator.gates.sequential.Clock;
import simulator.network.Link;
import simulator.sets.CUControllersSet;
import simulator.sets.InstructionSets;
import simulator.wrapper.wrappers.ProgramCounter;
import simulator.wrapper.wrappers.ShiftLeftLogical;
import simulator.wrapper.wrappers.SignExtension;
import simulator.wrapper.wrappers.*;

public class Sample {

    public static void main(String[] args) {

        //processor-clock
        Clock clock = new Clock("CLOCK1", 1000);

        Link[] pci = pcInitialize();

        //processor-ProgramCounter
        ProgramCounter pc = new ProgramCounter("PC", "32X32", clock.getOutput(0)
        // pass no signal, so All 32-bits are false as default
        );

        //creating number four
        Link[] four = new Link[32];
        for (int index = 0; index < 32; ++index) {
            four[index] = Simulator.falseLogic;
            if (index == 29)
                four[index] = Simulator.trueLogic;
        }

        //using Adder to add four to pc
        Adder pcAdder_newAddress = new Adder("PC_ADDER", "64X33");
        for (int index = 0; index < 32; ++index)
            pcAdder_newAddress.addInput(pc.getOutput(0));
        pcAdder_newAddress.addInput(four);


        //creating-Memory
        ByteMemory mem = new ByteMemory("MEMORY");
        Boolean[][] memInstruction = new Boolean[65536][8];


        //memory sets
        Boolean[][] instructions = getSampleMemoryInstruction();


        //assign to an [65536][8] matrix
        for(int row=0; row<40; row++) {
            for (int column=0; column<8; column++)
                memInstruction[row][column]=instructions[row][column];
        }

        //assign instructions into memory
        mem.setMemory(memInstruction);
        //make memory READ-ONLY
        mem.addInput(Simulator.falseLogic);

        /* ********************************************************************************************************** */

        //store pc address outputs
        Link[] memInstruct = new Link[16];
        for(int index = 0; index < 16; index++)
            memInstruct[index] = pc.getOutput(index + 16);

        /* ********************************************************************************************************** */

        //after reading we should store instructions
        InstructionSets instructionSets = new InstructionSets();
        //now we separate data
        separateMemoryData(mem, instructionSets);

        /* ********************************************************************************************************** */

        //sign extension 16-bit to 32-bit (For branches)
        SignExtension S16X32 = new SignExtension("SIGN_EX16TO32","16X32", instructionSets.immediate);

        //store the extension output
        Link[] extendedImmediate = new Link[32];
        for(int index = 0; index < 32; index++)
            extendedImmediate[index] = S16X32.getOutput(index);

        //shift left 16-bit immediate <<2       ~OUTPUT:18-bit
        ShiftLeftLogical shiftImmediate = new ShiftLeftLogical("IMM_SHIFT_LEFT_X2", "16<<2",
                extendedImmediate);

        /* ********************************************************************************************************** */

        // creating Control-Unit
        ControlUnit controlUnit = new ControlUnit("CONTROL_UNIT","6X9",
                instructionSets.opCode[0],
                instructionSets.opCode[1],
                instructionSets.opCode[2],
                instructionSets.opCode[3],
                instructionSets.opCode[4],
                instructionSets.opCode[5]
        );

        /* ********************************************************************************************************** */

        // For jumping to the targetAddress
        //shift left 26-bit targetAddress <<2        ~OUTPUT:28-bit
        ShiftLeftLogical shiftTargetAddress = new ShiftLeftLogical("TARGET_ADD_SHIFT_LEFT_X2", "26<<2",
                instructionSets.targetAddress);

        //store shifted targetAddress
        Link[] shiftedTargetAddress= new Link[28];
        for (int index = 0; index < 28; index++)
            shiftedTargetAddress[index] = shiftTargetAddress.getOutput(index);

        //make a new address for pc
        Link[] jumpAddress = new Link[32];
        for(int index = 0; index < 32; index ++){
            //first 4-bits must be current pc address
            if(index < 4)
                jumpAddress[index] = pc.getOutput(index);
            if(index >= 4)
                jumpAddress[index] = shiftedTargetAddress[index - 4];
        }
        /* ********************************************************************************************************** */

        //pass new Address to pc and jump to address
        //TODO - pc jump is incomplete!
        pc.addInput(jumpAddress);

        /* ********************************************************************************************************** */

        //Register-File Process
        RegisterFile registerFile = new RegisterFile("REG_FILE", "32X32", CUControllersSet.RegWrite);
        registerFile.addInput(instructionSets.resSource);
        Mux2X1 rtORrd = new Mux2X1("MUX2X1_0", "11X5", CUControllersSet.RegDst);
        rtORrd.addInput(instructionSets.resTarget); //as first arg
        rtORrd.addInput(instructionSets.resDestination); //as second arg

        //store Mux result
        Link[] rtORrd_Result = new Link[5];
        for (int index = 0; index < 6; index++)
            rtORrd_Result[index] = rtORrd.getOutput(index);
        registerFile.addInput(rtORrd_Result);

        //storing RD1
        Link[] RD1 = new Link[5];
        for (int index = 0; index < 6; index++)
            RD1[index] = registerFile.getOutput(index);

        //storing RD2
        Link[] RD2 = new Link[5];
        for (int index = 0; index < 6; index++)
            RD2[index] = registerFile.getOutput(index);

        /* ********************************************************************************************************** */

        //calculate SrcA & SrcB ALU (1-bit ALUSrc & 5-bits RD2 & 32-bits SignEImm)
        //convert RD2 to 32-bits
        Link[] newRD2 = new Link[32];
        for (int index = 0; index < 32; index++) {
            if (index >= 27)
                newRD2[index] = RD2[index - 27];
            else
                newRD2[index] = Simulator.falseLogic;
        }

        Mux2X1 rd2ORSignE = new Mux2X1("rd2ORSignE", "65X32", CUControllersSet.ALUSrc);
        rd2ORSignE.addInput(newRD2);
        rd2ORSignE.addInput(extendedImmediate);

        ALU alu1 = new ALU("ALU1", "64X32");
        alu1.addInput();
        //TODO: Continue

        /* ********************************************************************************************************** */

        Simulator.debugger.addTrackItem(clock, pc, mem);
        Simulator.debugger.setDelay(500);
        Simulator.circuit.startCircuit();
    }

    private static Link[] pcInitialize() {
        Link[] temp = new Link[32];
        for (Link each: temp)
            each = Simulator.falseLogic;
        return temp;
    }

    private static Boolean[][] getSampleMemoryInstruction() {

        return new Boolean[][]{{false,false,false,false,false, false,false,true},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},
                             {false,false,false,false,true,false,false,false},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},{false,false,false,false,false, true,false,false},
                             {false,false,false,false,false,false,false,true},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},
                             {false,false,false,false,false,false,false,true},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},
                             {false,false,false,false,false,false,false,true},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},
         /*beq $t1,$t2,1*/   {false,false,false,true,false, false,false,true},{false,false,true,false,true, false, true, false},{false,false,false,false,false,false,false,false},{false,false,false,false,false ,false,false,true},
                             {false,false,false,false,false,false,false,true},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,false},
         /* lw $t1,0x0($t1)*/{true,false, false, false,true, true,false,true},{false,false,false,false, true,false, false,true},{false,false,false,false, true,false, false,true},{false,false,false,false,false,false,false,false},
      /*lw $t2,0x1($t2)*/  	 {true,false,false, false, true,true, false,true},{false,false,false,false, true, false,true,false},{false,false,false,false,true, false, true,false},{false,false,false,false,false,false, false,true},
         /* sw $t1,0x1($t2)*/{true,false,true,false, true, true, false, true},{false,true,false,false, true, false, false,true},{false,false,false,false,false,false,false,false},{false,false,false,false,false,false,false,true}};
    }


    static void separateMemoryData(ByteMemory mem, InstructionSets sets){
        for(int i = 0 ;i < 32 ;i++) {
            if (i < 6) {
                sets.opCode[i] = mem.getOutput(i);
                sets.func[5-i]=mem.getOutput(i+26);
                if (i < 5){
                    sets.shiftAmount[i]=mem.getOutput(i+21);
                }
            }
            if(i <= 4){
                sets.resSource[i]=mem.getOutput((4-i)+6);
                sets.resTarget[i]=mem.getOutput((4-i)+11);
                sets.resDestination[4-i]=mem.getOutput(i+16);
            }
            if(i < 26){
                sets.targetAddress[i]=mem.getOutput(i+6);
            }
            if(i < 16){
                sets.immediate[i]=mem.getOutput(i+16);
            }
        }
    }
}