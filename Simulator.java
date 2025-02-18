import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Simulator{
    public Simulator(){
        memory=new Memory();
        clock=0;
        cores=new Cores[4];
        for(int i=0;i<4;i++){
            cores[i]=new Cores(i);
        }
        //testing purpose
//        cores[0].registers[2]=1;
//        cores[0].registers[3]=2;
//        cores[1].registers[2]=1;
//        cores[1].registers[3]=2;
//        cores[2].registers[2]=1;
//        cores[2].registers[3]=2;
//        cores[3].registers[2]=1;
//        cores[3].registers[3]=2;

        //testing for memory
        // Memory.memory[100]=30;
        // Memory.memory[104]=20;
        // Memory.memory[0] = 42;
        // Memory.memory[1] = 17;
        // Memory.memory[2] = 89;
        // Memory.memory[3] = 23;
        // Memory.memory[4] = 56;
        // Memory.memory[5] = 91;
        // Memory.memory[6] = 34;
        // Memory.memory[7] = 75;
        // Memory.memory[8] = 12;
        // Memory.memory[9] = 68;
        // Memory.memory[10] = 5;
        // Memory.memory[11] = 99;
        // Memory.memory[12] = 31;
        // Memory.memory[13] = 47;
        // Memory.memory[14] = 83;
        // Memory.memory[15] = 28;
        // Memory.memory[16] = 60;
        // Memory.memory[17] = 14;
        // Memory.memory[18] = 71;
        // Memory.memory[19] = 39;
        // // core 0 is initialized above
        // for (int i = 0; i < 20; i++) {
        //     Memory.memory[i + 1 * 256] = Memory.memory[i]; // Core 1
        //     Memory.memory[i + 2 * 256] = Memory.memory[i]; // Core 2
        //     Memory.memory[i + 3 * 256] = Memory.memory[i]; // Core 3
        // }
        labelMapping=new HashMap<>();
        opcodes=new HashSet<>(Set.of("ADD","SUB","MUL","MV","ADDI","MULI","AND","OR","XOR","ANDI","ORI","XORI","BNE","BEQ","JAL","JALR","LW","SW","LA","LI","BGE","BLT","J","JR","REM"));
    }

    //function for mapping all the labels with proper instruction number. 
    private void mapAllTheLabels(String[] program){
        for(int i=0;i<program.length;i++){
            String[] decodedInstruction = program[i].split(" ");
            if(!opcodes.contains(decodedInstruction[0].toUpperCase())){
                String label=decodedInstruction[0].trim().replace(":", "");
                labelMapping.put(label,i);
                // System.out.println("label: "+label+ " and pc: "+i);
            }
        }
    }

    public void printLabels(){
        for(Map.Entry<String,Integer> ele:labelMapping.entrySet()){
            System.out.println("Label:"+ele.getKey()+" Value:"+ele.getValue());
        }
        System.out.println();
    }

    public void initializeProgram(String[] program){
        this.program_Seq=program;
    }
    public void runProgram(){
        mapAllTheLabels(program_Seq);
        printLabels();
        boolean isDone=(cores[0].pc==program_Seq.length && cores[1].pc==program_Seq.length && cores[2].pc==program_Seq.length && cores[3].pc==program_Seq.length);
        while(!isDone){
            isInstruction=true;
            for(int i=0;i<4;i++){
                if(cores[i].pc>=program_Seq.length){
                    break;
                }
                this.cores[i].execute(program_Seq,labelMapping,memory);
            } 
            // printResult();
            // Memory.printMemory();
            if(isInstruction)
                this.clock++; 
            isDone=(cores[0].pc==program_Seq.length && cores[1].pc==program_Seq.length && cores[2].pc==program_Seq.length && cores[3].pc==program_Seq.length);
        }
    }
    public void printResult(){
        for(int i=0;i<4;i++){
            System.out.println("Core :"+i);
            for(int j=0;j<32;j++){
                System.out.print(cores[i].registers[j]+" ");
            }
            System.out.println();
        }
        System.out.println("The number of clock cycles taken are:"+this.clock);
    }


    private Memory memory;
    private int clock;
    private Cores[] cores;
    public String[] program_Seq;
    public Map<String,Integer> labelMapping;
    public Set<String> opcodes;
    public static boolean isInstruction;
}