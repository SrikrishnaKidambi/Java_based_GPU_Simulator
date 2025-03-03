import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Simulator{
    public Simulator(){
        clock=0;
        cores=new Core[4];
        for(int i=0;i<4;i++){
            cores[i]=new Core(i);
        }
        labelMapping=new HashMap<>();
        opcodes=new HashSet<>(Set.of("add","sub","mul","mv","addi","muli","and","or","xor","andi","ori","xori","rem","bne","beq","jal","jalr","lw","sw","la","li","bge","blt","j","jr","ecall"));
    }

    //function for mapping all the labels with proper instruction number. 
    private void mapAllTheLabels(String[] program){
        for(int i=0;i<program.length;i++){
            String[] decodedInstruction = program[i].trim().split(" ");
            if(!opcodes.contains(decodedInstruction[0].toUpperCase()) && decodedInstruction[0].contains(":")){
                String label=decodedInstruction[0].trim().replace(":", "");
                if(labelMapping.containsKey(label) && label!="" && !label.contains("#")) {
                	System.out.println("The label is that is already present is "+label+". yeah!!");
                	SimulatorGUI.console.append(label+"has already been used. Hence stopping the execution of program");
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
    public void runProgram(Memory mem,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping,Map<String,Integer>latencies){
        mapAllTheLabels(program_Seq);
        printLabels();
        System.out.println("Program execution started");
        boolean isDone=(cores[0].pc==program_Seq.length && cores[1].pc==program_Seq.length && cores[2].pc==program_Seq.length && cores[3].pc==program_Seq.length);
        InstructionState in1=new InstructionState();
        InstructionState in2=new InstructionState();
        InstructionState in3=new InstructionState();
        InstructionState in4=new InstructionState();
        InstructionState in5=new InstructionState();
        in5.isDummy=false;
        LinkedList<InstructionState>pipeLineQueue=new LinkedList<>();
        pipeLineQueue.addLast(in1);
        pipeLineQueue.addLast(in3);
        pipeLineQueue.addLast(in2);
        pipeLineQueue.addLast(in4);
        pipeLineQueue.addLast(in5);
        while(!isDone){
            isInstruction=true;
            for(int i=0;i<4;i++){
                if(cores[i].pc>=program_Seq.length){
                    break;
                }
                this.cores[i].execute(program_Seq, pipeLineQueue, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies);
            } 
            System.out.println("The pc value just completed execution is:"+cores[0].pc);
            System.out.println("The pc value just completed execution is:"+cores[1].pc);
            System.out.println("The pc value just completed execution is:"+cores[2].pc);
            System.out.println("The pc value just completed execution is:"+cores[3].pc);
            // if(pipeLineQueue.isEmpty()){
            //     break;
            // }
            pipeLineQueue.removeFirst();
            //if(cores[0].pc<=program_Seq.length-1){
                InstructionState new_in=new InstructionState();
                new_in.isDummy=false;
                pipeLineQueue.addLast(new_in);
            //}
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
        while(!pipeLineQueue.isEmpty()){

            cores[0].execute(program_Seq, pipeLineQueue, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies);
            cores[1].execute(program_Seq, pipeLineQueue, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies);
            cores[2].execute(program_Seq, pipeLineQueue, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies);
            cores[3].execute(program_Seq, pipeLineQueue, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies);
            if(pipeLineQueue.removeFirst()==cores[0].lastInstruction){
                System.out.println("Sriman thopu dammunte aapu");
                break;
            }
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
        SimulatorGUI.console.append("\nThe number of clock cycles taken for execution are "+this.clock);
        labelMapping.clear();
        System.out.println("Printing the labels map after clearing:");
        this.clock=0;
        SimulatorGUI.console.append("\nThe number of stalls of core 0 are: "+cores[0].totalStalls);
        SimulatorGUI.console.append("\nThe number of stalls of core 1 are: "+cores[1].totalStalls);
        SimulatorGUI.console.append("\nThe number of stalls of core 2 are: "+cores[2].totalStalls);
        SimulatorGUI.console.append("\nThe number of stalls of core 3 are: "+cores[3].totalStalls);
    }


    public int clock;
    public Core[] cores;
    public String[] program_Seq;
    public Map<String,Integer> labelMapping;
    public Set<String> opcodes;
    public static boolean isInstruction;
    public boolean isPipelineForwardingEnabled;
}