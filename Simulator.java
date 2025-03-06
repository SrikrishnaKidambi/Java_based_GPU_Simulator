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
    public void runProgram(Memory mem,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping,Map<String,Integer>latencies){
        mapAllTheLabels(program_Seq);
        printLabels();
        System.out.println(program_Seq.length);
        System.out.println("Program execution started");
        boolean isDone=(cores[0].pc==program_Seq.length && cores[1].pc==program_Seq.length && cores[2].pc==program_Seq.length && cores[3].pc==program_Seq.length);
        while(!isDone){
            isInstruction=true;
            for(int i=0;i<4;i++){
                if(cores[i].pc>=program_Seq.length){
                    break;
                }
                this.cores[i].executeUtil(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping);
            } 
            // if(pipeLineQueue.isEmpty()){
            //     break;
            // }
            
            //}
            // printResult();
            // Memory.printMemory();
            if(isInstruction) {
            	this.clock++;
            }    
//            System.out.println("The value of pc in core 0 is :"+cores[0].pc);
//            System.out.println("The value of pc in core 1 is :"+cores[1].pc);
//            System.out.println("The value of pc in core 2 is :"+cores[2].pc);
//            System.out.println("The value of pc in core 3 is :"+cores[3].pc);
            isDone=(cores[0].pc==program_Seq.length && cores[1].pc==program_Seq.length && cores[2].pc==program_Seq.length && cores[3].pc==program_Seq.length);
            System.out.println();
            
        }
        boolean firstPipelineDone=false;
        boolean secondPipelineDone=false;
        boolean thirdPipelineDone=false;
        boolean fourthPipelineDone=false;
        
        while(!firstPipelineDone || !secondPipelineDone || !thirdPipelineDone || !fourthPipelineDone){
        	if(!firstPipelineDone) {
        		cores[0].execute(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping);
        	}
        	if(!secondPipelineDone) {
        		cores[1].execute(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping);
        	}
        	if(!thirdPipelineDone) {
        		cores[2].execute(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping);
        	}
        	if(!fourthPipelineDone) {
        		cores[3].execute(program_Seq, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping);
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
//    public void hazardDetector(String[] program){
//        for(int i=0;i<program.length;i++){
//            dataHazardsMapping.put(i, 0);
//        }
//        for(int curr_idx=0;curr_idx<program.length;curr_idx++){
//            String[] splitInstruction=program[curr_idx].trim().replace(","," ").split("\\s");
//            String opcode=splitInstruction[0];
//            switch (opcode) {
//                case "add":
//                case "sub":
//                case "mul":
//                case "rem":
//                case "and":
//                case "or":
//                case "xor":
//                case "addi":
//                case "andi":
//                case "ori":
//                case "xori":
//                case "mv":
//                case "lw":
//                case "jal":
//                case "jalr":
//                case "la":
//                case "li":
//                    int rem_instructions=program.length-1-curr_idx;
//                    if(rem_instructions>=3){
//                        String[] insN1=program[curr_idx+1].trim().replace(","," ").split("\\s");
//                        String[] insN2=program[curr_idx+2].trim().replace(","," ").split("\\s");
//                        String[] insN3=program[curr_idx+3].trim().replace(","," ").split("\\s");
//                        System.out.println("The next 1 for pc" + curr_idx+"is: "+insN1[0]);
//                        System.out.println("The next 2 for pc" + curr_idx+"is: "+insN2[0]);
//                        System.out.println("The next 3 for pc" + curr_idx+"is: "+insN3[0]);
//                        String rdCurr=splitInstruction[1];
//                        if(insN1[0].equals("j") || insN1[0].equals("jr")|| insN1[0].contains(":") ){
//                            System.out.println("Saiman thopu");
//                        }
//                        else if((rdCurr.equals("x17") || rdCurr.equals("X17") || rdCurr.equals("X10") || rdCurr.equals("x10")) && insN1[0].equals("ecall")){
//                            dataHazardsMapping.put(curr_idx+1, 3+dataHazardsMapping.get(curr_idx));
//                            if(curr_idx==0){
//                                System.out.println("if2:Value pushed in map: "+dataHazardsMapping.get(0));
//                            }
//                        }
//                        else if(insN1[0].equals("ecall")){
//                            if(curr_idx==0){
//                                System.out.println("if:Value pushed in map: "+dataHazardsMapping.get(0));
//                            }
//                        }
//                        else if(rdCurr.equals(insN1[2]) || (insN1.length==4 && rdCurr.equals(insN1[3]))){
//                            dataHazardsMapping.put(curr_idx+1, 3+dataHazardsMapping.get(curr_idx));
//                            if(curr_idx==0){
//                                System.out.println("correct if:Value pushed in map: "+dataHazardsMapping.get(0));
//                            }
//                        }
//                        if(insN2[0].equals("j") || insN2[0].equals("jr")  || insN2[0].contains(":")){
//                            System.out.println("Saiman thopu");
//                        }
//                        else if((rdCurr.equals("x17") || rdCurr.equals("X17") || rdCurr.equals("X10") || rdCurr.equals("x10")) && insN2[0].equals("ecall")){
//                            dataHazardsMapping.put(curr_idx+2, 2+dataHazardsMapping.get(curr_idx));
//                            if(curr_idx==0){
//                                System.out.println("if3:Value pushed in map: "+dataHazardsMapping.get(0));
//                            }
//                        }
//                        else if(insN2[0].equals("ecall")){
//                            if(curr_idx==0){
//                                System.out.println("if:Value pushed in map: "+dataHazardsMapping.get(0));
//                            }
//                        }
//                        else if(rdCurr.equals(insN2[2]) || (insN2.length==4 && rdCurr.equals(insN2[3]))){
//                            dataHazardsMapping.put(curr_idx+2, 2+dataHazardsMapping.get(curr_idx));
//                        }
//                        if(insN3[0].equals("j") || insN3[0].equals("jr") || insN3[0].contains(":")){
//                            System.out.println("Saiman thopu");
//                        }
//                        else if((rdCurr.equals("x17") || rdCurr.equals("X17") || rdCurr.equals("X10") || rdCurr.equals("x10")) && insN3[0].equals("ecall")){
//                            dataHazardsMapping.put(curr_idx+3, 1+dataHazardsMapping.get(curr_idx));
//                            if(curr_idx==0){
//                                System.out.println("if4:Value pushed in map: "+dataHazardsMapping.get(0));
//                            }
//                        }
//                        else if(insN3[0].equals("ecall")){
//                            if(curr_idx==0){
//                                System.out.println("if:Value pushed in map: "+dataHazardsMapping.get(0));
//                            }
//                        }
//                        else if(rdCurr.equals(insN3[2]) || (insN3.length==4 && rdCurr.equals(insN3[3]))){
//                            dataHazardsMapping.put(curr_idx+3, 1+dataHazardsMapping.get(curr_idx));
//                        }
//                    }
//                    else if(rem_instructions>=2){
//                        String[] insN1=program[curr_idx+1].trim().replace(","," ").split("\\s");
//                        String[] insN2=program[curr_idx+2].trim().replace(","," ").split("\\s");
//                        String rdCurr=splitInstruction[1];
//                        if(insN1[0].equals("j") || insN1[0].equals("jr")|| insN1[0].contains(":") ){
//                            System.out.println("Saiman thopu");
//                        }
//                        else if((rdCurr.equals("x17") || rdCurr.equals("X17") || rdCurr.equals("X10") || rdCurr.equals("x10")) && insN1[0].equals("ecall")){
//                            dataHazardsMapping.put(curr_idx+1, 3+dataHazardsMapping.get(curr_idx));
//                        }
//                        else if(insN1[0].equals("ecall")){
//
//                        }
//                        else if(rdCurr.equals(insN1[2]) || (insN1.length==4 && rdCurr.equals(insN1[3]))){
//                            dataHazardsMapping.put(curr_idx+1, 3+dataHazardsMapping.get(curr_idx));
//                        }
//                        if(insN2[0].equals("j") || insN2[0].equals("jr")  || insN2[0].contains(":")){
//                            System.out.println("Saiman thopu");
//                        }
//                        else if((rdCurr.equals("x17") || rdCurr.equals("X17") || rdCurr.equals("X10") || rdCurr.equals("x10")) && insN2[0].equals("ecall")){
//                            dataHazardsMapping.put(curr_idx+2, 2+dataHazardsMapping.get(curr_idx));
//                        }
//                        else if(insN2[0].equals("ecall") ){
//
//                        }
//                        else if(rdCurr.equals(insN2[2]) || (insN2.length==4 && rdCurr.equals(insN2[3]))){
//                            dataHazardsMapping.put(curr_idx+2, 2+dataHazardsMapping.get(curr_idx));
//                        }
//                    }
//                    else if(rem_instructions>=1){
//                        String[] insN1=program[curr_idx+1].trim().replace(","," ").split("\\s");
//                        String rdCurr=splitInstruction[1];
//                        if(insN1[0].equals("j") || insN1[0].equals("jr")  ||  insN1[0].contains(":") ){
//
//                        }
//                        else if((rdCurr.equals("x17") || rdCurr.equals("X17") || rdCurr.equals("X10") || rdCurr.equals("x10")) && insN1[0].equals("ecall")){
//                            dataHazardsMapping.put(curr_idx+1, 3+dataHazardsMapping.get(curr_idx));
//                        }
//                        else if(insN1[0].equals("ecall")){
//
//                        }
//                        else if(rdCurr.equals(insN1[2]) || (insN1.length==4 && rdCurr.equals(insN1[3]))){
//                            dataHazardsMapping.put(curr_idx+1, 3+dataHazardsMapping.get(curr_idx));
//                        }
//                    }
//                    break; 
//                default:
//                    break;
//            }
//        }
//    }
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
        SimulatorGUI.console.append("\n");
    }

    public int clock;
    public Core[] cores;
    public String[] program_Seq;
    public Map<String,Integer> labelMapping;
    public Set<String> opcodes;
    public static boolean isInstruction;
    public Map<Integer,Integer> dataHazardsMapping;
    public boolean isPipelineForwardingEnabled;
}