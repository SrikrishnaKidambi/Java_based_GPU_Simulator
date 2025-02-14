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
        cores[0].registers[2]=1;
        cores[0].registers[3]=2;
        cores[1].registers[2]=1;
        cores[1].registers[3]=2;
        cores[2].registers[2]=1;
        cores[2].registers[3]=2;
        cores[3].registers[2]=1;
        cores[3].registers[3]=2;

        //testing for memory
        Memory.memory[100]=30;
        Memory.memory[104]=20;
        labelMapping=new HashMap<>();
        opcodes=new HashSet<>(Set.of("ADD","SUB","MUL","MV","ADDI","MULI","AND","OR","XOR","ANDI","ORI","XORI","BNE","BEQ","JAL","JALR","LW","SW"));
    }

    //function for mapping all the labels with proper instruction number. 
    private void mapAllTheLabels(String[] program){
        for(int i=0;i<program.length;i++){
            String[] decodedInstruction = program[i].split(" ");
            if(!opcodes.contains(decodedInstruction[0].toUpperCase())){
                String label=decodedInstruction[0].replace(":", "");
                labelMapping.put(label,i);
            }
        }
    }

    public void initializeProgram(String[] program){
        this.program_Seq=program;
    }
    public void runProgram(){
        mapAllTheLabels(program_Seq);
        while(this.clock<program_Seq.length){
            for(int i=0;i<4;i++){
                if(cores[i].pc>=program_Seq.length){
                    break;
                }
                this.cores[i].execute(program_Seq,labelMapping,memory);
            }  
            this.clock++; 
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
    }
    private Memory memory;
    private int clock;
    private Cores[] cores;
    public String[] program_Seq;
    public Map<String,Integer> labelMapping;
    public Set<String> opcodes;
}