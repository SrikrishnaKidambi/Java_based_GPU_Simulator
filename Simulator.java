import java.util.ArrayList;
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
        dataHazardsMapping=new HashMap<>();
        // instructionsExecuted=new int[4];
        // for(int i=0;i<4;i++){
        //     instructionsExecuted[i]=0;
        // }
        opcodes=new HashSet<>(Set.of("add","sub","mul","mv","addi","muli","and","or","xor","andi","ori","xori","rem","bne","beq","jal","jalr","lw","sw","la","li","bge","blt","j","jr","ecall"));
    }

    
    public static FetcherResult IF(int pc,InstructionState in,int coreID,String[] program,InstructionState last,int[] instructionsExecuted) {
    	if(coreID==0) {
        	if(in.isDummy || in==null || in.IF_done_core0==true){
                return new FetcherResult(last,pc);
            }
        }else if(coreID==1) {
        	if(in.isDummy || in==null || in.IF_done_core1==true){
        		return new FetcherResult(last,pc);
            }
        }else if(coreID==2) {
        	if(in.isDummy || in==null || in.IF_done_core2==true){
        		return new FetcherResult(last,pc);
            }
        }else if(coreID==3) {
        	if(in.isDummy || in==null || in.IF_done_core3==true){
        		return new FetcherResult(last,pc);
            }
        }
    	if(program[pc].contains(":")) {
    		pc++;
    	}
    	in.instruction=program[pc];
    	in.pc_val=pc;
    	pc++;
        instructionsExecuted[0]++;
        // instructionsExecuted[coreID]++;
//    	in.isDummy=false;
    	if(pc==program.length) {
    		last=in;
    	}
    	if(coreID==0) {
        	in.IF_done_core0=true;
        }
        if(coreID==1) {
        	in.IF_done_core1=true;
        }
        if(coreID==2) {
        	in.IF_done_core2=true;
        }
        if(coreID==3) {
        	in.IF_done_core3=true;
        }	
        return new FetcherResult(last,pc);
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
                labelMapping.put(label,i+1);
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
    public void runProgram(Memory mem,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping,Map<String,Integer>latencies,boolean isPipelineForwardingEnabled){
        mapAllTheLabels(program_Seq);
        printLabels();
        System.out.println(program_Seq.length);
        System.out.println("Program execution started");
        this.clock+=4;
        boolean isDone=(cores[0].pc==program_Seq.length && cores[1].pc==program_Seq.length && cores[2].pc==program_Seq.length && cores[3].pc==program_Seq.length);
        while(!isDone){
            // isInstruction=true;
            for(int i=0;i<4;i++){
                if(cores[i].pc>=program_Seq.length){
                    continue;
                }
                this.cores[i].executeUtil(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping,isPipelineForwardingEnabled);
            } 
            // if(pipeLineQueue.isEmpty()){
            //     break;
            // }
            
            //}
            // printResult();
            // Memory.printMemory();
            //if(isInstruction) {
            	this.clock++;
            //}   
//            System.out.println("The values in core 0");
//            for(int i=0;i<32;i++){
//                System.out.print(cores[0].registers[i]+" ");
//            } 
            System.out.println();
            System.out.println("The value of pc in core 0 is :"+cores[0].pc);
            System.out.println("The value of pc in core 1 is :"+cores[1].pc);
            System.out.println("The value of pc in core 2 is :"+cores[2].pc);
            System.out.println("The value of pc in core 3 is :"+cores[3].pc);
            isDone=(cores[0].pc==program_Seq.length && cores[1].pc==program_Seq.length && cores[2].pc==program_Seq.length && cores[3].pc==program_Seq.length);
            System.out.println();
        }
        boolean firstPipelineDone=false;
        boolean secondPipelineDone=false;
        boolean thirdPipelineDone=false;
        boolean fourthPipelineDone=false;
        
        while(!firstPipelineDone || !secondPipelineDone || !thirdPipelineDone || !fourthPipelineDone){
            System.out.println("The values in core 0");
            for(int i=0;i<32;i++){
                System.out.print(cores[0].registers[i]+" ");
            } 
            System.out.println();
        	if(!firstPipelineDone) {
        		cores[0].execute(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping,isPipelineForwardingEnabled);
        	}
        	if(!secondPipelineDone) {
        		cores[1].execute(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping,isPipelineForwardingEnabled);
        	}
        	if(!thirdPipelineDone) {
        		cores[2].execute(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping,isPipelineForwardingEnabled);
        	}
        	if(!fourthPipelineDone) {
        		cores[3].execute(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping,isPipelineForwardingEnabled);
        	}
            if(!firstPipelineDone) {
            	InstructionState first_top=cores[0].pipeLineQueue.get(0);
//            	System.out.println("the last instruction in core 0 is:"+cores[0].lastInstruction.pc_val);
//            	System.out.println("the last instruction in core 0 is:"+first_top.pc_val);
            	
                if(first_top==cores[0].lastInstruction) {
                	System.out.println("the last instruction in core 0 is:"+cores[0].lastInstruction.pc_val);
                	firstPipelineDone=true;
                	cores[0].pipeLineQueue.removeFirst();
                }else {
                	cores[0].pipeLineQueue.removeFirst();
                }
            }
            if(!secondPipelineDone) {
            	InstructionState second_top=cores[1].pipeLineQueue.getFirst();
                if(second_top==cores[1].lastInstruction) {
                	System.out.println("the last instruction in core 1 is:"+cores[1].lastInstruction.pc_val);
                	secondPipelineDone=true;
                	cores[1].pipeLineQueue.removeFirst();
                }else {
                	cores[1].pipeLineQueue.removeFirst();
                }
            }
            if(!thirdPipelineDone) {
            	InstructionState third_top=cores[2].pipeLineQueue.getFirst();
                if(third_top==cores[2].lastInstruction) {
                	System.out.println("the last instruction in core 2 is:"+cores[2].lastInstruction.pc_val);
                	thirdPipelineDone=true;
                	cores[2].pipeLineQueue.removeFirst();
                }else {
                	cores[2].pipeLineQueue.removeFirst();
                }
            }
            if(!fourthPipelineDone) {
            	InstructionState fourth_top=cores[3].pipeLineQueue.getFirst();
                if(fourth_top==cores[3].lastInstruction) {
                	System.out.println("the last instruction in core 3 is:"+cores[3].lastInstruction.pc_val);
                	fourthPipelineDone=true;
                	cores[3].pipeLineQueue.removeFirst();
                }else {
                	cores[3].pipeLineQueue.removeFirst();
                }
            }
            
        }
    }
    public void printResult(Map<String,Integer>latencies){
        for(int i=0;i<4;i++){
            System.out.println("Core :"+i);
            for(int j=0;j<32;j++){
                System.out.print(cores[i].registers[j]+" ");
            }
            System.out.println();
        }
        System.out.println("The number of clock cycles taken are:"+(this.clock-this.labelMapping.size()));
        SimulatorGUI.console.append("\nThe number of clock cycles taken for execution are "+(this.clock-1));
        // labelMapping.clear();
        // System.out.println("Printing the labels map after clearing:");
        // this.clock=0;
        int latency_offset=latencies.getOrDefault("addi",0)-1;
        SimulatorGUI.console.append("\nThe number of stalls of core 0 are: "+(cores[0].totalStalls-latency_offset));
        SimulatorGUI.console.append("\nThe number of stalls of core 1 are: "+(cores[1].totalStalls-latency_offset));
        SimulatorGUI.console.append("\nThe number of stalls of core 2 are: "+(cores[2].totalStalls-latency_offset));
        SimulatorGUI.console.append("\nThe number of stalls of core 3 are: "+(cores[3].totalStalls-latency_offset));
        SimulatorGUI.console.append("\n");

        // calculation of total number of instructions executed and IPC
        int maxNumberofInstructions=Math.max(Math.max(cores[0].instructionsExecuted[0], cores[1].instructionsExecuted[0]),Math.max(cores[2].instructionsExecuted[0], cores[3].instructionsExecuted[0]));
        double IPC=(double)maxNumberofInstructions/this.clock;
        double IPC0=(double)cores[0].instructionsExecuted[0]/this.clock;
        double IPC1=(double)cores[1].instructionsExecuted[0]/this.clock;
        double IPC2=(double)cores[2].instructionsExecuted[0]/this.clock;
        double IPC3=(double)cores[3].instructionsExecuted[0]/this.clock;
        SimulatorGUI.console.append("\nIPC : "+IPC+"\n");
        SimulatorGUI.console.append("IPC for all the cores: \n");
        SimulatorGUI.console.append("IPC of core 0: "+IPC0+"\n");
        SimulatorGUI.console.append("IPC of core 1: "+IPC1+"\n");
        SimulatorGUI.console.append("IPC of core 2: "+IPC2+"\n");
        SimulatorGUI.console.append("IPC of core 3: "+IPC3+"\n");
    }

    public int clock;
    public Core[] cores;
    public String[] program_Seq;
    public Map<String,Integer> labelMapping;
    public Set<String> opcodes;
    public static boolean isInstruction;
    public Map<Integer,Integer> dataHazardsMapping;
    // public int[] instructionsExecuted;
}