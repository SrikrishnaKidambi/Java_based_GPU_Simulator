import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Simulator{
    public Simulator(Cache_L1D L1_Cache0,Cache_L1D L1_Cache1,Cache_L1D L1_Cache2,Cache_L1D L1_Cache3,Cache_L1I L1_Cache0_I,Cache_L1I L1_Cache1_I,Cache_L1I L1_Cache2_I,Cache_L1I L1_Cache3_I,Cache_L2 L2_cache){
        clock=0;
        caches=new Cache_L1D[4];
        caches_I=new Cache_L1I[4];
        caches[0]=L1_Cache0;
        caches[1]=L1_Cache1;
        caches[2]=L1_Cache2;
        caches[3]=L1_Cache3;
        caches_I[0]=L1_Cache0_I;
        caches_I[1]=L1_Cache1_I;
        caches_I[2]=L1_Cache2_I;
        caches_I[3]=L1_Cache3_I;
        
        this.L2_cache=L2_cache;
        cores=new Core[4];
        for(int i=0;i<4;i++){
            cores[i]=new Core(i,caches[i],caches_I[i],L2_cache);
        }
        labelMapping=new HashMap<>();
        dataHazardsMapping=new HashMap<>();
        // instructionsExecuted=new int[4];
        // for(int i=0;i<4;i++){
        //     instructionsExecuted[i]=0;
        // }
        opcodes=new HashSet<>(Set.of("add","sub","mul","mv","addi","muli","and","or","xor","andi","ori","xori","rem","bne","beq","jal","jalr","lw","sw","la","li","bge","blt","j","jr","ecall","SYNC","lw_spm","sw_spm"));
        global_PC=0;
        global_sync_counter=0;
        synced=new int[4];
    }

    
    public static FetcherResult IF(int pc,InstructionState in,int coreID,String[] program,InstructionState last,int[] instructionsExecuted,Cache_L1D L1_Cache,Cache_L2 L2_Cache,Cache_L1I L1_Cache_I,Memory mem) {
    	if(coreID==0) {
        	if(in.isDummy || in==null || in.IF_done_core0==true){
                return new FetcherResult(last,pc,0,0);
            }
        }else if(coreID==1) {
        	if(in.isDummy || in==null || in.IF_done_core1==true){
        		return new FetcherResult(last,pc,0,0);
            }
        }else if(coreID==2) {
        	if(in.isDummy || in==null || in.IF_done_core2==true){
        		return new FetcherResult(last,pc,0,0);
            }
        }else if(coreID==3) {
        	if(in.isDummy || in==null || in.IF_done_core3==true){
        		return new FetcherResult(last,pc,0,0);
            }
        }
    	
    	MemoryAccess memAccess=null;
    	memAccess=new MemoryAccess(L1_Cache,L2_Cache,L1_Cache_I,mem);
        MemoryResult res=memAccess.readInstruction(4092-4*pc);
        pc=res.result;
    	if(program[pc].contains(":")) {
            pc++;
    	}
//    	global_PC=Math.min(pc, global_PC);
    	if(global_PC==pc) {
            in.instruction=program[pc];
            in.pc_val=pc;
    		if(in.instruction.equals("SYNC") && synced[coreID]==1){
                pc++;
                in.instruction=program[pc];
                in.pc_val=pc;
                memAccess.readInstruction(4092-4*pc);
                synced[coreID]=0;
            }
            if(in.instruction.equals("SYNC")){
                global_sync_counter++;
                if(global_sync_counter%4!=0){
                    L1_Cache.flushCache(L2_Cache, mem, coreID);
                    return new FetcherResult(last, pc,0, 0);
                }
                else if(global_sync_counter%4==0){
                    synced[0]=1;
                    synced[1]=1;
                    synced[2]=1;
                    synced[3]=1;
                    synced[coreID]=0;
                    L1_Cache.flushCache(L2_Cache, mem,coreID);
                    // SimulatorGUI.console.append("Instructions got synced!!!!!!!!!!!\n");
                    // SimulatorGUI.console.append("The values at memory[1020]="+mem.memory[1020]+"\n");
                    // SimulatorGUI.console.append("The values at memory[1024]="+mem.memory[1024]+"\n");
                    // SimulatorGUI.console.append("The values at memory[1028]="+mem.memory[1028]+"\n");
                    // SimulatorGUI.console.append("The values at memory[1032]="+mem.memory[1032]+"\n");
                    global_sync_counter=0;
                    // return new FetcherResult(last, pc,0, 0);
                }
                
            }
        	pc++;
            instructionsExecuted[0]++;
            // instructionsExecuted[coreID]++;
//        	in.isDummy=false;
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
            return new FetcherResult(last,pc,res.latency,0);
    	}else {
    		return new FetcherResult(last,pc,0,1);
    	}
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
//        this.clock+=4;
        boolean isDone=(cores[0].pc==program_Seq.length && cores[1].pc==program_Seq.length && cores[2].pc==program_Seq.length && cores[3].pc==program_Seq.length);
        while(!isDone){
        	global_PC=Math.min(cores[0].pc, Math.min(cores[1].pc, Math.min(cores[2].pc, cores[3].pc)));
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
        System.out.println("------------------------------- The length of the pipline is "+cores[0].pipeLineQueue.size());
        
        while(!firstPipelineDone || !secondPipelineDone || !thirdPipelineDone || !fourthPipelineDone){
        	global_PC=Math.min(cores[0].pc, Math.min(cores[1].pc, Math.min(cores[2].pc, cores[3].pc)));
        	this.clock++;
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
        SimulatorGUI.console.append("\n");
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

        SimulatorGUI.console.append("Stats for L1 Cache (Data):");
        SimulatorGUI.console.append("\nMiss Rate in core 0:"+((double)(caches[0].misses)/(caches[0].accesses)));
        SimulatorGUI.console.append("\nMiss Rate in core 1:"+((double)(caches[1].misses)/(caches[1].accesses)));
        SimulatorGUI.console.append("\nMiss Rate in core 2:"+((double)(caches[2].misses)/(caches[2].accesses)));
        SimulatorGUI.console.append("\nMiss Rate in core 3:"+((double)(caches[3].misses)/(caches[3].accesses)));

        SimulatorGUI.console.append("\nStats for L1 Cache (Instruction):");
        SimulatorGUI.console.append("\nMiss Rate in core 0:"+((double)(caches_I[0].misses)/(caches_I[0].accesses)));
        SimulatorGUI.console.append("\nMiss Rate in core 1:"+((double)(caches_I[1].misses)/(caches_I[1].accesses)));
        SimulatorGUI.console.append("\nMiss Rate in core 2:"+((double)(caches_I[2].misses)/(caches_I[2].accesses)));
        SimulatorGUI.console.append("\nMiss Rate in core 3:"+((double)(caches_I[3].misses)/(caches_I[3].accesses)));

        SimulatorGUI.console.append("\nStats for L2 Cache:");
        SimulatorGUI.console.append("\nMiss Rate:"+((double)(L2_cache.misses)/(L2_cache.accesses)));
    }


    public int clock;
    public Core[] cores;
    public String[] program_Seq;
    public Map<String,Integer> labelMapping;
    public Set<String> opcodes;
    public static boolean isInstruction;
    public Map<Integer,Integer> dataHazardsMapping;
    public Cache_L1D[] caches;
    public Cache_L1I[] caches_I;
    public Cache_L2 L2_cache;
    public static int global_PC;
    public static int global_sync_counter;
    public static int[] synced;
    // public int[] instructionsExecuted;
}