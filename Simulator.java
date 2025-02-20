import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Simulator{
    public Simulator(){
        clock=0;
        cores=new Cores[4];
        for(int i=0;i<4;i++){
            cores[i]=new Cores(i);
        }
        labelMapping=new HashMap<>();
        opcodes=new HashSet<>(Set.of("add","sub","mul","mv","addi","muli","and","or","xor","andi","ori","xori","bne","beq","jal","jalr","lw","sw","la","li","bge","blt","j","jr","ecall"));
    }

    //function for mapping all the labels with proper instruction number. 
    private void mapAllTheLabels(String[] program){
        for(int i=0;i<program.length;i++){
            String[] decodedInstruction = program[i].trim().split(" ");
            if(!opcodes.contains(decodedInstruction[0].toUpperCase()) && decodedInstruction[0].contains(":")){
                String label=decodedInstruction[0].trim().replace(":", "");
                if(labelMapping.containsKey(label) && label!="" && !label.contains("#")) {
                	System.out.println("The label is that is already present is "+label+". yeah!!");
                	throw new IllegalArgumentException("Duplicate label found");
                }
                labelMapping.put(label,i);
                // System.out.println("label: "+label+ " and pc: "+i);
            }
        }
    }

    public void printLabels(){
    	System.out.println("Printing the labels ---------------------------:");
        for(Map.Entry<String,Integer> ele:labelMapping.entrySet()){
            System.out.println("Label:"+ele.getKey()+" Value:"+ele.getValue());
        }
        System.out.println();
    }

    public void initializeProgram(String[] program){
        this.program_Seq=program;
    }
    public void runProgram(Memory mem,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping){
        mapAllTheLabels(program_Seq);
        printLabels();
        System.out.println("Program execution started");
        boolean isDone=(cores[0].pc==program_Seq.length && cores[1].pc==program_Seq.length && cores[2].pc==program_Seq.length && cores[3].pc==program_Seq.length);
        while(!isDone){
            isInstruction=true;
            for(int i=0;i<4;i++){
                if(cores[i].pc>=program_Seq.length){
                    break;
                }
                this.cores[i].execute(program_Seq, labelMapping, mem, stringVariableMapping, nameVariableMapping);
            } 
            // printResult();
            // Memory.printMemory();
            if(isInstruction)
                this.clock++; 
//            System.out.println("The value of pc in core 0 is :"+cores[0].pc);
//            System.out.println("The value of pc in core 1 is :"+cores[1].pc);
//            System.out.println("The value of pc in core 2 is :"+cores[2].pc);
//            System.out.println("The value of pc in core 3 is :"+cores[3].pc);
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
        labelMapping.clear();
        System.out.println("Printing the labels map after clearing:");
        this.clock=0;
    }


    private int clock;
    private Cores[] cores;
    public String[] program_Seq;
    public Map<String,Integer> labelMapping;
    public Set<String> opcodes;
    public static boolean isInstruction;
}