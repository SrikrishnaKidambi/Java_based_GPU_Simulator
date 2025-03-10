import java.util.LinkedList;
import java.util.Map;


public class Core {

    public Core(int coreID){
        this.coreID=coreID;
        this.pc=0;
        this.registers=new int[32];
        registers[0]=0;
        this.cc=0;
        this.controlStalls=0;
        this.latencyStalls=0;
        this.totalStalls=0;
        instructionsExecuted=new int[1];
        this.instructionsExecuted[0]=0;
        pipeLineQueue=new LinkedList<>();
        InstructionState in1=new InstructionState();
        InstructionState in2=new InstructionState();
        InstructionState in3=new InstructionState();
        InstructionState in4=new InstructionState();
        InstructionState in5=new InstructionState();
        in5.isDummy=false;
        pipeLineQueue.addLast(in1);
        pipeLineQueue.addLast(in3);
        pipeLineQueue.addLast(in2);
        pipeLineQueue.addLast(in4);
        pipeLineQueue.addLast(in5);
    }
    
    public void executeUtil(String[] program,Memory mem,Map<String,Integer>labelMapping,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping,Map<String,Integer>latencies,Map<Integer,Integer> dataHazardsMapping,boolean isPipelineForwardingEnabled) {
    	execute(program, mem, labelMapping, stringVariableMapping, nameVariableMapping,latencies,dataHazardsMapping,isPipelineForwardingEnabled);
    	System.out.println("Size of the pipeline queue:"+pipeLineQueue.size());
    	pipeLineQueue.removeFirst();
        InstructionState new_in=new InstructionState();
        new_in.isDummy=false;
        pipeLineQueue.addLast(new_in);
        
    }
    public void execute(String[] program,Memory mem,Map<String,Integer>labelMapping,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping,Map<String,Integer>latencies,Map<Integer,Integer> dataHazardsMapping,boolean isPipelineForwardingEnabled){
        if(pipeLineQueue.size()>=1){
            InstructionState in1=pipeLineQueue.get(0);
            WB(in1);
        }
        if(pipeLineQueue.size()>=2){
            InstructionState in2=pipeLineQueue.get(1);
            if(isPipelineForwardingEnabled){
                int dataStalls=hazardDetectorUtil(pipeLineQueue,isPipelineForwardingEnabled,1);
                totalStalls+=dataStalls;
                for(int i=0;i<dataStalls;i++) {
                    pipeLineQueue.add(1+i, new InstructionState());
                }
            }
            in2=pipeLineQueue.get(1);
            System.out.println("@@@@Calling mem for the opcode "+in2.opcode);
            MEM(in2, mem);
        }
        if(pipeLineQueue.size()>=3){
            if(this.coreID==0){
                // System.out.println("Number of latency stalls :"+this.latencyStalls);
            }
            for(int i=0;i<latencyStalls;i++){
                pipeLineQueue.add(2+i, new InstructionState());
            }
            latencyStalls=0;  //making the number of stalls due to latency to zero
            InstructionState in3=pipeLineQueue.get(2);
            if(isPipelineForwardingEnabled){
                int dataStalls=hazardDetectorUtil(pipeLineQueue,isPipelineForwardingEnabled,2);
                System.out.println("Total number of datastalls req by branch at pc: "+ in3.pc_val+ " are "+dataStalls);
                totalStalls+=dataStalls;
                for(int i=0;i<dataStalls;i++) {
                    pipeLineQueue.add(2+i, new InstructionState());
                }
            }
            in3=pipeLineQueue.get(2);
            EX(in3, labelMapping, stringVariableMapping, nameVariableMapping);
        }
        if(pipeLineQueue.size()>=4){
            InstructionState in4=pipeLineQueue.get(3);
            System.out.println("PC of the instruction that is getting executed:"+in4.pc_val);
            System.out.println("The fetched instruction is dummy:"+in4.isDummy);
            if(isPipelineForwardingEnabled){
                decode(in4);
                if(!in4.isDummy && (in4.opcode.equals("bne") || in4.opcode.equals("beq") || in4.opcode.equals("blt") || in4.opcode.equals("bge"))){
                    int dataStalls=hazardDetectorUtil(pipeLineQueue,isPipelineForwardingEnabled,3);
                    System.out.println("Total number of datastalls req by branch at pc: "+ in4.pc_val+ " are "+dataStalls);
                    totalStalls+=dataStalls;
                    for(int i=0;i<dataStalls;i++) {
                        pipeLineQueue.add(3+i, new InstructionState());
                    }
                }
            }
            in4 = pipeLineQueue.get(3);
            ID_RF(pipeLineQueue,in4, labelMapping, stringVariableMapping, nameVariableMapping,latencies);
            if(!isPipelineForwardingEnabled){
                int dataStalls=hazardDetectorUtil(pipeLineQueue,isPipelineForwardingEnabled,4);
                totalStalls+=dataStalls;
                for(int i=0;i<dataStalls;i++) {
                    pipeLineQueue.add(3+i, new InstructionState());
                }
            }
        }
        if(pipeLineQueue.size()>=5){
            InstructionState in5=pipeLineQueue.get(4);
            if(this.coreID==0){
                // System.out.println("Number of control stalls:"+this.controlStalls);
            }
            if(this.coreID==0) {
            	System.out.println("Number of control stalls:"+this.controlStalls);
            }
            if(this.controlStalls>0){
                in5.isDummy=true;
                this.controlStalls--;
            }
            if(this.coreID==0) {
            	if(this.pc==program.length && in5.IF_done_core0==false){
                    // if(in5.pc_val==8){
                    //     // System.out.println("You are making last instruction as dummy here");
                    // }
                    in5.isDummy=true;
                }
            }
            if(this.coreID==1) {
            	if(this.pc==program.length && in5.IF_done_core1==false){
                    // if(in5.pc_val==8){
                    //     // System.out.println("You are making last instruction as dummy here");
                    // }
                    in5.isDummy=true;
                }
            }
            if(this.coreID==2) {
            	if(this.pc==program.length && in5.IF_done_core2==false){
                    // if(in5.pc_val==8){
                    //     // System.out.println("You are making last instruction as dummy here");
                    // }
                    in5.isDummy=true;
                }
            }
            if(this.coreID==3) {
            	if(this.pc==program.length && in5.IF_done_core3==false){
                    // if(in5.pc_val==8){
                    //     // System.out.println("You are making last instruction as dummy here");
                    // }
                    in5.isDummy=true;
                }
            }
//            if(this.pc==program.length){
//                lastInstruction=in5;
//            }
            if(this.pc==program.length-1){
                // System.out.println("The instruction that is going to get executed is dummy or not:"+in5.isDummy);
            }
            if(this.coreID==0) {
            	System.out.println("The instruction is dummy:"+in5.pc_val);
            	System.out.println("The instruction is dummy:"+in5.isDummy);
            }
//            IF(program, in5);
            FetcherResult temp=Simulator.IF(pc, in5, coreID, program, lastInstruction,instructionsExecuted);
            this.pc=temp.pc_val;
            lastInstruction=temp.lastInstruction;
            System.out.println("Printing the pipeline:");
           for(int i=0;i<5;i++) {
            System.out.print(pipeLineQueue.get(i).pc_val+" ");
           }
           System.out.println();
        }
    //     System.out.println("--------------------------------Printing the Pipeline after execution for core :"+this.coreID);
    //     for(int i=0;i<Math.min(pipeLineQueue.size(), 5);i++) {
    //     	System.out.print(pipeLineQueue.get(i).pc_val+" ");
    //     }
    //     System.out.println();
    // }
    }
    private String instructionParser(String instruction) {
	  
        int hashSymbolIdx=instruction.indexOf("#");
        if(hashSymbolIdx!=-1 && hashSymbolIdx==0) {
            if(hashSymbolIdx>0 && instruction.charAt(hashSymbolIdx-1)!=' ') {
                throw new IllegalArgumentException("Invalid comment. Space is missing after the instruction");
            }
            return instruction.substring(0,hashSymbolIdx).trim();
        }
        return instruction.trim();
    }
//    private void IF(String[] program,InstructionState in){
//        if(this.coreID==0) {
//        	if(in.isDummy || in==null || in.IF_done_core0==true){
//        		if(this.coreID==0) {
//            		System.out.println("----------------------The pipeline currently :");
//            		for(int i=0;i<=4;i++) {
//                		System.out.print(pipeLineQueue.get(i).pc_val+" ");
//                	}
//                	System.out.println();
//            	}
//                return;
//            }
//        }else if(this.coreID==1) {
//        	if(in.isDummy || in==null || in.IF_done_core1==true){
//        		if(this.coreID==0) {
//            		System.out.println("----------------------The pipeline currently :");
//            		for(int i=0;i<=4;i++) {
//                		System.out.print(pipeLineQueue.get(i).pc_val+" ");
//                	}
//                	System.out.println();
//            	}
//                return;
//            }
//        }else if(this.coreID==2) {
//        	if(in.isDummy || in==null || in.IF_done_core2==true){
//        		if(this.coreID==0) {
//            		System.out.println("----------------------The pipeline currently :");
//            		for(int i=0;i<=4;i++) {
//                		System.out.print(pipeLineQueue.get(i).pc_val+" ");
//                	}
//                	System.out.println();
//            	}
//                return;
//            }
//        }if(this.coreID==3) {
//        	if(in.isDummy || in==null || in.IF_done_core3==true){
//        		if(this.coreID==0) {
//            		System.out.println("----------------------The pipeline currently :");
//            		for(int i=0;i<=4;i++) {
//                		System.out.print(pipeLineQueue.get(i).pc_val+" ");
//                	}
//                	System.out.println();
//            	}
//                return;
//            }
//        }
//        if(program[pc].contains(":")){
//            pc++;
//        }
//        in.instruction=program[pc];
//        in.pc_val=pc;
//        if(coreID==0){
//            System.out.println("The value of pc in IF:"+this.pc+" for opcode:"+in.opcode);
//        }
//        pc++;
//        if(this.coreID==0) {
//    		System.out.println("----------------------The pipeline currently :");
//    		for(int i=0;i<=4;i++) {
//        		System.out.print(pipeLineQueue.get(i).pc_val+" ");
//        	}
//        	System.out.println();
//        	System.out.println("The total number of stalls so far are: "+this.totalStalls);
//    	}
//        if(this.pc==program.length){
//            lastInstruction=in;
//            System.out.println("Fetched the last instruction successfully with pc value"+in.pc_val);
//        }
//        
////        in.isDummy=false;
//        if(this.coreID==0) {
//        	in.IF_done_core0=true;
//        }
//        if(this.coreID==1) {
//        	in.IF_done_core1=true;
//        }
//        if(this.coreID==2) {
//        	in.IF_done_core2=true;
//        }
//        if(this.coreID==3) {
//        	in.IF_done_core3=true;
//        }	
//        return;
//    }
    private void decode(InstructionState in){
        if(in.isDummy){
            return;
        }
        String instruction=in.instruction;
        String parsedInstruction = null;
        try {
            parsedInstruction=instructionParser(instruction);
        }catch(IllegalArgumentException e) {
            System.err.println("Error occured is:"+e.getMessage());
        }
        String[] decodedInstruction = parsedInstruction.trim().replace(","," ").split("\\s+");  //neglecting the commas that are put between registers.
        in.opcode=decodedInstruction[0].trim();
        if(in.opcode.equals("bne") || in.opcode.equals("beq") || in.opcode.equals("blt") || in.opcode.equals("bge")){
        	if(parsedInstruction.contains("CID")){
        		return;
        	}
            in.rs1=Integer.parseInt(decodedInstruction[1].substring(1));
            in.rs2=Integer.parseInt(decodedInstruction[2].substring(1));
        }
        return;
    }
    private void ID_RF(LinkedList<InstructionState>pipeLineQueue,InstructionState in,Map<String,Integer>labelMapping,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping,Map<String,Integer>latencies){
        if(this.coreID==0) {
        	if(in.isDummy || in==null || in.IDRF_done_core0==true){
                // if(in.pc_val==8){   // hardcoded currently please check
                //     System.out.println("The last instruction is treated as dummy");
                // }
                return;
            }
        }else if(this.coreID==1) {
        	if(in.isDummy || in==null || in.IDRF_done_core1==true){
                // if(in.pc_val==8){   // hardcoded currently please check
                //     System.out.println("The last instruction is treated as dummy");
                // }
                return;
            }
        }else if(this.coreID==2) {
        	if(in.isDummy || in==null || in.IDRF_done_core2==true){
                // if(in.pc_val==8){   // hardcoded currently please check
                //     System.out.println("The last instruction is treated as dummy");
                // }
                return;
            }
        }else if(this.coreID==3) {
        	if(in.isDummy || in==null || in.IDRF_done_core3==true){
                // if(in.pc_val==8){   // hardcoded currently please check
                //     System.out.println("The last instruction is treated as dummy");
                // }
                return;
            }
        }
        String instruction=in.instruction;
        String parsedInstruction = null;
        try {
            parsedInstruction=instructionParser(instruction);
        }catch(IllegalArgumentException e) {
            System.err.println("Error occured is:"+e.getMessage());
        }
               
        String[] decodedInstruction = parsedInstruction.trim().replace(","," ").split("\\s+");  //neglecting the commas that are put between registers.
        in.opcode=decodedInstruction[0].trim();
        if(coreID==0){
            System.out.println("The value of pc in ID/RF:"+this.pc+" for opcode:"+in.opcode);
        } 
        int latency=0;
        switch (in.opcode) {
            case "add":
                //Ex: add x1 x2 x3
                in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    System.out.println("Incorrent instruction add");
                    System.exit(0);
                }
                if(this.coreID==0) {
                	if(!in.IDRF_done_once0) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_once1) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_once2) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_once3) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "sub":
                //Ex: sub x1 x2 x3
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    System.out.println("Incorrent instruction sub");
                    System.exit(0);
                }
                if(this.coreID==0) {
                	if(!in.IDRF_done_once0) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_once1) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_once2) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_once3) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "mul":
                //Ex: mul x1 x2 x3
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    System.out.println("Incorrent instruction mul");
                    System.exit(0);
                }
                if(this.coreID==0) {
                	if(!in.IDRF_done_once0) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_once1) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_once2) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_once3) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "mv":
                //Ex: mv x1 x2
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(this.coreID==0) {
                	if(!in.IDRF_done_once0) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_once1) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_once2) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_once3) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "addi":
                //Ex: addi x1 x2 8
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    System.out.println("Incorrent instruction addi");
                    System.exit(0);
                }
                else{
                    in.immediateVal=Integer.parseInt(decodedInstruction[3].substring(0));
                }
                if(this.coreID==0) {
                	if(!in.IDRF_done_once0) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_once1) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_once2) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_once3) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "muli":
                //Ex: muli x1 x2 3
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    System.out.println("Incorrent instruction muli");
                    System.exit(0);
                }
                else{
                    in.immediateVal=Integer.parseInt(decodedInstruction[3].substring(0));
                }
                if(this.coreID==0) {
                	if(!in.IDRF_done_once0) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_once1) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_once2) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_once3) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "rem":
                //Ex: rem x1 x2 x3
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    System.out.println("Incorrent instruction rem");
                    System.exit(0);
                }
                if(this.coreID==0) {
                	if(!in.IDRF_done_once0) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_once1) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_once2) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_once3) {
                		latency=latencies.get(in.opcode);
                        latencyStalls+=latency-1;
                        totalStalls+=latency-1;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
			case "and": 
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				}else{
                    System.out.println("Incorrent instruction and");
                    System.exit(0);
				}
				break;
			case "or": 
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				}else{
                    System.out.println("Incorrent instruction or");
                    System.exit(0);
				}
				break;
			case "xor": 
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				}else{
                    System.out.println("Incorrent instruction xor");
                    System.exit(0);
				}
				break;
			case "andi": 
				in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    System.out.println("Incorrent instruction andi");
                    System.exit(0);
				}
				else{
					in.immediateVal=Integer.parseInt(decodedInstruction[3].substring(0));
				} 
				break;
			case "ori": 
				in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    System.out.println("Incorrent instruction ori");
                    System.exit(0);
				}
				else{
					in.immediateVal=Integer.parseInt(decodedInstruction[3].substring(0));
				} 
				break;
			case "xori": 
				in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    System.out.println("Incorrent instruction xori");
                    System.exit(0);
				}
				else{
					in.immediateVal=Integer.parseInt(decodedInstruction[3].substring(0));
				} 
				break;
			case "bne":
				int temp4_rs1=0,temp4_rs2=0;
                if(!decodedInstruction[1].equals("CID")) {
                	in.rs1= Integer.parseInt(decodedInstruction[1].substring(1));
                    in.rs2=Integer.parseInt(decodedInstruction[2].substring(1));
                    in.labelName=decodedInstruction[3];
                    temp4_rs1=registers[in.rs1];
                    temp4_rs2=registers[in.rs2];
                }
                if(decodedInstruction[1].equals("CID")){
                    temp4_rs1=this.coreID;
                    temp4_rs2=Integer.parseInt(decodedInstruction[2]);
                    in.labelName=decodedInstruction[3];
                }
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        temp4_rs1=in.pipeline_reg[0];
                        temp4_rs2=in.pipeline_reg[1];
                    }else if(in.pipeline_reg[0]!=null){
                        temp4_rs1=in.pipeline_reg[0];
                        temp4_rs2=registers[in.rs2];
                    }else if(in.pipeline_reg[1]!=null){
                        temp4_rs1=registers[in.rs1];
                        temp4_rs2=in.pipeline_reg[1];
                    }
                }
				if(temp4_rs1!=temp4_rs2){
					pc=labelMapping.get(in.labelName).intValue();
				}else {
                	pc=in.pc_val+1;
                }
                if(this.coreID==0) {
                	if(!in.IDRF_done_core0) {
                		this.controlStalls++;
                        totalStalls++;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_core1) {
                		this.controlStalls++;
                        totalStalls++;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_core2) {
                		this.controlStalls++;
                        totalStalls++;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_core3) {
                		this.controlStalls++;
                        totalStalls++;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
				break;
			case "blt":
				int temp1_rs1=0,temp1_rs2=0;
                if(!decodedInstruction[1].equals("CID")) {
                	in.rs1= Integer.parseInt(decodedInstruction[1].substring(1));
                    in.rs2=Integer.parseInt(decodedInstruction[2].substring(1));
                    in.labelName=decodedInstruction[3];
                    temp1_rs1=registers[in.rs1];
                    temp1_rs2=registers[in.rs2];
                }
                if(decodedInstruction[1].equals("CID")){
                    temp1_rs1=this.coreID;
                    temp1_rs2=Integer.parseInt(decodedInstruction[2]);
                    in.labelName=decodedInstruction[3];
                }
                if(in.isfowarded ){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        temp1_rs1=in.pipeline_reg[0];
                        temp1_rs2=in.pipeline_reg[1];
                    }else if(in.pipeline_reg[0]!=null){
                        temp1_rs1=in.pipeline_reg[0];
                        temp1_rs2=registers[in.rs2];
                    }else if(in.pipeline_reg[1]!=null){
                        temp1_rs1=registers[in.rs1];
                        temp1_rs2=in.pipeline_reg[1];
                    }
                }
				if(temp1_rs1<temp1_rs2){
					pc=labelMapping.get(in.labelName).intValue();
				}else {
                	pc=in.pc_val+1;
                }
				if(this.coreID==0) {
                	if(!in.IDRF_done_core0) {
                		this.controlStalls++;
                        totalStalls++;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_core1) {
                		this.controlStalls++;
                        totalStalls++;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_core2) {
                		this.controlStalls++;
                        totalStalls++;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_core3) {
                		this.controlStalls++;
                        totalStalls++;
                	}
                }
                
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
				break;
			case "sw": 
				// syntax of instruction: sw x10 4(x5)
                // this means that from the register x10 take the value and store it in the memory location of x5 with an offset of 4.
				in.rs2=Integer.parseInt(decodedInstruction[1].substring(1));
				String[] offsetAndRegBase=decodedInstruction[2].split("[()]");
				in.immediateVal=Integer.parseInt(offsetAndRegBase[0]);
				in.rs1=Integer.parseInt(offsetAndRegBase[1].substring(1));  //here the address of the register that has the base address of the memory location is being stored.
				// if(in.immediateVal<-512 || in.immediateVal>512){
				// 	System.out.println("Immediate value cannot be less than -512 or greater than 512");
				// 	System.exit(0);
				// }
				break;
			case "jalr":
				// syntax : jalr x1 x2 x0 -> store the value of pc+4 in x1 and jump to x2+x0
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				if(this.coreID==0) {
                	if(!in.IDRF_done_core0) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_core1) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_core2) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_core3) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
				break;
			case "jr" : 
				in.rs1=Integer.parseInt(decodedInstruction[1].substring(1));
				if(this.coreID==0) {
                	if(!in.IDRF_done_core0) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_core1) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_core2) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_core3) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
				break;
			case "la" :
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.labelName=decodedInstruction[2];  // this indicates variable name
                System.out.println("The label name obtained is:"+in.labelName);
				if((decodedInstruction[1].equals("a0") || decodedInstruction[1].equals("x10") || decodedInstruction[1].equals("X10")) && stringVariableMapping.containsKey(in.labelName)) {
					a_0=stringVariableMapping.get(in.labelName);
				  //   System.out.println("The string that is printed due to ecall for variable name "+variableName+" is: " + a_0);
					break;
				}
                in.immediateVal=nameVariableMapping.get(in.labelName);  // this indicates the value that has to be loaded into the register directly
				break;
            case "bge":
                //Ex: bge x1 x2 label
                
                int temp2_rs1=0,temp2_rs2=0;
                if(!decodedInstruction[1].equals("CID")) {
                	in.rs1= Integer.parseInt(decodedInstruction[1].substring(1));
                    in.rs2=Integer.parseInt(decodedInstruction[2].substring(1));
                    in.labelName=decodedInstruction[3];
                    temp2_rs1=registers[in.rs1];
                    temp2_rs2=registers[in.rs2];
                }
                if(decodedInstruction[1].equals("CID")){
                    temp2_rs1=this.coreID;
                    temp2_rs2=Integer.parseInt(decodedInstruction[2]);
                    in.labelName=decodedInstruction[3];
                }
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        temp2_rs1=in.pipeline_reg[0];
                        temp2_rs2=in.pipeline_reg[1];
                    }else if(in.pipeline_reg[0]!=null){
                        temp2_rs1=in.pipeline_reg[0];
                        temp2_rs2=registers[in.rs2];
                    }else if(in.pipeline_reg[1]!=null){
                        temp2_rs1=registers[in.rs1];
                        temp2_rs2=in.pipeline_reg[1];
                    }
                }
                if(temp2_rs1>=temp2_rs2){
                    pc=labelMapping.get(in.labelName).intValue();
                }else {
                	pc=in.pc_val+1;
                }
                if(this.coreID==0) {
                	if(!in.IDRF_done_core0) {
                		this.controlStalls+=1;
                        totalStalls+=1;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_core1) {
                		this.controlStalls+=1;
                        totalStalls+=1;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_core2) {
                		this.controlStalls+=1;
                        totalStalls+=1;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_core3) {
                		this.controlStalls+=1;
                        totalStalls+=1;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "beq":
                //Ex: beq x1 x2 label

            	int temp3_rs1=0,temp3_rs2=0;
                if(!decodedInstruction[1].equals("CID") ) {
                	in.rs1= Integer.parseInt(decodedInstruction[1].substring(1));
                    in.rs2=Integer.parseInt(decodedInstruction[2].substring(1));
                    in.labelName=decodedInstruction[3];
                    temp3_rs1=registers[in.rs1];
                    temp3_rs2=registers[in.rs2];
                }
                if(decodedInstruction[1].equals("CID")){
                    temp3_rs1=this.coreID;
                    temp3_rs2=Integer.parseInt(decodedInstruction[2]);
                    in.labelName=decodedInstruction[3];
                }
                if(in.isfowarded){
                    System.out.println("Forwarding is happening");
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        temp3_rs1=in.pipeline_reg[0];
                        temp3_rs2=in.pipeline_reg[1];
                    }else if(in.pipeline_reg[0]!=null){
                        temp3_rs1=in.pipeline_reg[0];
                        temp3_rs2=registers[in.rs2];
                    }else if(in.pipeline_reg[1]!=null){
                        temp3_rs1=registers[in.rs1];
                        temp3_rs2=in.pipeline_reg[1];
                    }
                }
                System.out.println("The forwarded values in beq are:"+temp3_rs1+" and "+temp3_rs2);
                if(temp3_rs1==temp3_rs2){
                    pc=labelMapping.get(in.labelName).intValue();
                }else {
                	pc=in.pc_val+1;
                }
                if(this.coreID==0) {
                	if(!in.IDRF_done_core0) {
                		this.controlStalls+=1;
                        totalStalls+=1;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_core1) {
                		this.controlStalls+=1;
                        totalStalls+=1;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_core2) {
                		this.controlStalls+=1;
                        totalStalls+=1;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_core3) {
                		this.controlStalls+=1;
                        totalStalls+=1;
                	}
                }
//                System.out.println("The value of IDRF_done:"+in.IDRF_done+" for the core:"+this.coreID);
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "lw":
                //Ex: lw x1 8(x2) where 8 is the offset/immediate value and x2 is the base register
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                int paramStart=0;
                int paramEnd=0;
                for(int i=0;i<decodedInstruction[2].length();i++){
                    if(decodedInstruction[2].charAt(i)=='('){
                        paramStart=i;
                    }
                    if(decodedInstruction[2].charAt(i)==')'){
                        paramEnd=i;
                        break;
                    }
                }
                in.immediateVal=Integer.parseInt(decodedInstruction[2].substring(0,paramStart));
                // if(in.immediateVal<-512 || in.immediateVal>512){
                //     System.out.println("The immediate value can only be between -512 and 512"); // as the total memory the core can access is 1024 bytes so using immediate I can go 512bytes up and 512 bytes down
                //     System.exit(0);
                //     break;
                // }
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(paramStart+2,paramEnd));
                break;
            case "li":
                //Ex: li x1 8
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.immediateVal=Integer.parseInt(decodedInstruction[2]);
                // if(in.immediateVal>=256 || in.immediateVal<0){
                //   System.out.println("Cannot access the requested memory location");
                //   System.exit(0);
                //   break;
                // }
                break;
            case "jal":
                //Ex: jal x1 label
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.labelName=decodedInstruction[2];
                System.out.println("The label name in ID for : "+in.opcode+" is "+in.labelName);
                // pipeLineQueue.add(new InstructionState());
                // pipeLineQueue.add(new InstructionState()); 
                if(this.coreID==0) {
                	if(!in.IDRF_done_core0) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_core1) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_core2) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_core3) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);  
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);            
                break;
            case "j":
                //Ex: j label which is equivalent to jal x0 label
                in.labelName=decodedInstruction[1];
                if(this.coreID==0) {
                	if(!in.IDRF_done_core0) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==1) {
                	if(!in.IDRF_done_core1) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==2) {
                	if(!in.IDRF_done_core2) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                if(this.coreID==3) {
                	if(!in.IDRF_done_core3) {
                		this.controlStalls+=2;
                        totalStalls+=2;
                	}
                }
                // if(coreID==0)
                // System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                // System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
			case "ecall":
            System.out.println("You entered ecall in ID/RF");
				// nee time inka raledu bro
				break;
            default:
			Simulator.isInstruction=false;
			if(!labelMapping.containsKey(in.opcode.trim().replace(":", "")) && !in.opcode.equals("")) {
				System.out.println(in.opcode.trim()+" is an invalid opcode");
				SimulatorGUI.console.append(in.opcode.trim()+" is an invalid opcode. So program execution is stopped!");
				throw new IllegalArgumentException(in.opcode.trim()+" is an invalid opcode");
			}
                break;
        }
        if(this.coreID==0) {
        	in.IDRF_done_core0=true;
        }
        if(this.coreID==1) {
        	in.IDRF_done_core1=true;
        }
        if(this.coreID==2) {
        	in.IDRF_done_core2=true;
        }
        if(this.coreID==3) {
        	in.IDRF_done_core3=true;
        }
    }
    private void EX(InstructionState in,Map<String,Integer>labelMapping,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping){
        if(this.coreID==0) {
        	if(in.isDummy || in==null || in.EX_done_core0==true){
        		System.out.println("*********Returning for core0 because the instruction is dummy:"+in.isDummy);
                return;
            }
        }else if(this.coreID==1) {
        	if(in.isDummy || in==null || in.EX_done_core1==true){
                return;
            }
        }else if(this.coreID==2) {
        	if(in.isDummy || in==null || in.EX_done_core2==true){
                return;
            }
        }else if(this.coreID==3) {
        	if(in.isDummy || in==null || in.EX_done_core3==true){
                return;
            }
        }
        if(coreID==0){
            System.out.println("The value of pc in EX:"+this.pc+" for opcode:"+in.opcode);
        }        
        switch (in.opcode) {
            case "add":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0]+in.pipeline_reg[1];
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0]+registers[in.rs2];
                    }
                    else if(in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[1]+registers[in.rs1];
                    }
                    break;
                }
                in.result = registers[in.rs1] + registers[in.rs2];
                break;
            case "sub":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0]-in.pipeline_reg[1];
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0]-registers[in.rs2];
                    }
                    else if(in.pipeline_reg[1]!=null){
                        in.result=registers[in.rs1]+in.pipeline_reg[1];
                    }
                    break;
                }
                in.result = registers[in.rs1] - registers[in.rs2];
                break;
            case "mul":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0]*in.pipeline_reg[1];
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0]*registers[in.rs2];
                    }
                    else if(in.pipeline_reg[1]!=null){
                        in.result=registers[in.rs1]*in.pipeline_reg[1];
                    }
                    break;
                }
                in.result = registers[in.rs1] * registers[in.rs2];
                break;
            case "mv":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0];
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0];
                    }
                    break;
                }
            	in.result = registers[in.rs1];
            	System.out.println("Printing the core values till 15 for checking in core:"+this.coreID);
            	for(int i=0;i<11;i++) {
            		System.out.print(registers[i]+" ");
            	}
            	System.out.println("Done printing the core values(debugging)");
                break;
            case "addi":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0]+in.immediateVal;
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0]+in.immediateVal;
                    }
                    break;
                }
                in.result = registers[in.rs1] + in.immediateVal;
                break;
            case "muli":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0]*in.immediateVal;
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0]*in.immediateVal;
                    }
                    break;
                }
                in.result = registers[in.rs1] * in.immediateVal;
                break;
            case "rem":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0]%in.pipeline_reg[1];
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0]%registers[in.rs2];
                    }
                    else if(in.pipeline_reg[1]!=null){
                        in.result=registers[in.rs1]%in.pipeline_reg[1];
                    }
                    break;
                }
                in.result = registers[in.rs1] % registers[in.rs2];
                break;
            case "lw":
                // if(registers[in.rs1]+in.immediateVal+this.coreID>=1024 || registers[in.rs1]+in.immediateVal+this.coreID<0){
                //     System.out.println("The memory address requested is not accessible by the core "+registers[in.rs1]+in.immediateVal+this.coreID);
                //     System.exit(0);
                //     break;
                // }
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        // in.addressIdx=in.pipeline_reg[0]+in.immediateVal+this.coreID;
                        in.addressIdx=in.pipeline_reg[0]+in.immediateVal;
                    }
                    else if(in.pipeline_reg[0]!=null){
                        // in.addressIdx=in.pipeline_reg[0]+in.immediateVal+this.coreID;
                        in.addressIdx=in.pipeline_reg[0]+in.immediateVal;
                    }
                    break;
                }
                
                // in.addressIdx=registers[in.rs1]+in.immediateVal+this.coreID;
                in.addressIdx=registers[in.rs1]+in.immediateVal+0;   // loading the value that is stored in the memory of core zero
                break;
            case "li":
                in.result=in.immediateVal;
                break;
            case "jal":
                in.result=pc;
                System.out.println("The label name is "+in.labelName);
                pc=labelMapping.get(in.labelName).intValue();
                break;
            case "j":
                pc=labelMapping.get(in.labelName).intValue();
                break;
			case "jr":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null){
                        pc=in.pipeline_reg[0];
                    }
                }
				pc=registers[in.rs1];
				break;
			case "and":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0]&in.pipeline_reg[1];
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0]&registers[in.rs2];
                    }
                    else if(in.pipeline_reg[1]!=null){
                        in.result=registers[in.rs1]&in.pipeline_reg[1];
                    }
                    break;
                }
                in.result=registers[in.rs1] & registers[in.rs2];
				break;
			case "or":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0] | in.pipeline_reg[1];
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0] | registers[in.rs2];
                    }
                    else if(in.pipeline_reg[1]!=null){
                        in.result=registers[in.rs1] | in.pipeline_reg[1];
                    }
                    break;
                }
                in.result=registers[in.rs1] | registers[in.rs2];
				break;
			case "xor":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0] ^ in.pipeline_reg[1];
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0] ^ registers[in.rs2];
                    }
                    else if(in.pipeline_reg[1]!=null){
                        in.result=registers[in.rs1] ^ in.pipeline_reg[1];
                    }
                    break;
                }
                in.result=registers[in.rs1] ^ registers[in.rs2];
				break;
			case "andi":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0] & in.immediateVal;
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0]  & in.immediateVal;
                    }
                    break;
                }
                in.result=registers[in.rs1] & in.immediateVal;
				break;
			case "ori":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0] | in.immediateVal;
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0] | in.immediateVal;
                    }
                    break;
                }
                in.result=registers[in.rs1] | in.immediateVal;
				break;
			case "xori":
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        in.result=in.pipeline_reg[0] ^ in.immediateVal;
                    }
                    else if(in.pipeline_reg[0]!=null){
                        in.result=in.pipeline_reg[0] ^ in.immediateVal;
                    }
                    break;
                }
                in.result=registers[in.rs1] ^ in.immediateVal;
				break;
			case "bne":
				// pass the other conditional branch instructions in the same way for execution phase
				break;
			case "sw":
				// if((registers[in.rs1]+in.immediateVal+this.coreID)>=1024 || (registers[in.rs1]+in.immediateVal+this.coreID)<0){
				// 	System.out.println("Memory out of bounds");
				// 	System.exit(0);
				// }
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        // in.addressIdx=in.pipeline_reg[0]+in.immediateVal+this.coreID;
                        in.addressIdx=in.pipeline_reg[0]+in.immediateVal;    
                    }
                    else if(in.pipeline_reg[0]!=null){
                        // in.addressIdx=in.pipeline_reg[0]+in.immediateVal+this.coreID;
                        in.addressIdx=in.pipeline_reg[0]+in.immediateVal;
                    }
                    System.out.println("-------------------------The value in x16 :"+registers[16]+" in the core "+this.coreID);
                }
                System.out.println("-------------------------The value in x16 :"+registers[16]+" in the core "+this.coreID);
				// in.addressIdx=registers[in.rs1]+in.immediateVal+this.coreID;
                in.addressIdx=registers[in.rs1]+in.immediateVal+0;  // accessing the memory value that is stored at core 0
				break;
			case "jalr": 
				in.result=pc;
                if(in.isfowarded){
                    if(in.pipeline_reg[0]!=null && in.pipeline_reg[1]!=null){
                        pc=in.pipeline_reg[0]+in.pipeline_reg[1];
                    }else if(in.pipeline_reg[0]!=null){
                        pc=in.pipeline_reg[0]+registers[in.rs2];
                    }else if(in.pipeline_reg[1]!=null){
                        pc=registers[in.rs1]+in.pipeline_reg[1];
                    }
                }
				pc=registers[in.rs1]+registers[in.rs2];
				break;
			case "la":
				in.result=in.immediateVal;
				break;
			case "ecall":
                  // a0 -x10 and a7 - x17 please maintain these in the code
                    
        	  	  int a7=registers[17];  // register x17 is used for identification of the data type of the value to be printed.
                    if(in.isfowarded){
                        if(in.pipeline_reg[1]!=null){
                            a7=in.pipeline_reg[1];
                        }
                    }
                //   System.out.println("The value of a7 is "+a7);
        	  	  switch(a7) {
        	  	  		case 1:
        	  	  			int a0=registers[10];
                            if(in.isfowarded){
                                if(in.pipeline_reg[0]!=null){
                                    a0=in.pipeline_reg[0];
                                }
                            }
        	  	  			// System.out.print(a0);
                            System.out.println("Printing the value that has to be printed using ecall"+a0);
        	  	  			if(this.coreID==0) {
        	  	  				SimulatorGUI.console.append(a0+"");
        	  	  			}
        	  	  			break;
        	  	  		case 4:
                            // System.out.println("Printing as per request of mogambo");
        	  	  			// System.out.print(a_0);
                            System.out.println("Printing the value that has to be printed using ecall"+a_0);
        	  	  			if(this.coreID==0) {
        	  	  				SimulatorGUI.console.append(a_0);
        	  	  			}
        	  	  			break;
        	  	  		default:
        	  	  			break;
        	  	  }
        	  	  break;
            default:
                break;
        }
        if(this.coreID==0) {
        	in.EX_done_core0=true;
        }
        if(this.coreID==1) {
        	in.EX_done_core1=true;
        }
        if(this.coreID==2) {
        	in.EX_done_core2=true;
        }
        if(this.coreID==3) {
        	in.EX_done_core3=true;
        }
    }
    private void MEM(InstructionState in,Memory mem){
        if(this.coreID==0) {
        	if(in.isDummy || in==null || in.MEM_done_core0==true){
                return;
            }
        }else if(this.coreID==1) {
        	if(in.isDummy || in==null || in.MEM_done_core1==true){
                return;
            }
        }if(this.coreID==2) {
        	if(in.isDummy || in==null || in.MEM_done_core2==true){
                return;
            }
        }if(this.coreID==3) {
        	if(in.isDummy || in==null || in.MEM_done_core3==true){
                return;
            }
        }
        if(coreID==0){
            System.out.println("The value of pc in MEM:"+this.pc+" for opcode:"+in.opcode);
        }		
        switch (in.opcode) {
            case "lw":
                in.result=mem.memory[in.addressIdx];
                break;
            case "sw":
                if(in.isfowarded){
                    if(in.pipeline_reg[1]!=null){
                        mem.memory[in.addressIdx]=in.pipeline_reg[1];
                        System.out.println("The value that is stored in registers[16]:"+registers[16]);
                        System.out.println("-----------THe address index in the core "+coreID+" is "+in.addressIdx);
                        break;
                    }
                }
                System.out.println("The value that is stored in registers[16]:"+registers[16]);
                System.out.println("-----------THe address index in the core "+coreID+" is "+in.addressIdx);
                mem.memory[in.addressIdx]=registers[in.rs2];
                if(coreID==0)
                    mem.printMemory();
                break;
            default:
                break;
        }
        if(this.coreID==0) {
        	in.MEM_done_core0=true;
        }
        if(this.coreID==1) {
        	in.MEM_done_core1=true;
        }
        if(this.coreID==2) {
        	in.MEM_done_core2=true;
        }
        if(this.coreID==3) {
        	in.MEM_done_core3=true;
        }
    }
    private void WB(InstructionState in){
        	if(this.coreID==0) {
            	if(in.isDummy || in==null || in.WB_done_core0==true){
                    return;
                }
            }
        	if(this.coreID==1) {
            	if(in.isDummy || in==null || in.WB_done_core1==true){
                    return;
                }
            }
        	if(this.coreID==2) {
            	if(in.isDummy || in==null || in.WB_done_core2==true){
                    return;
                }
            }
        	if(this.coreID==3) {
            	if(in.isDummy || in==null || in.WB_done_core3==true){
                    return;
                }
            }
        if(coreID==0){
            System.out.println("The value of pc in WB:"+this.pc+" for opcode:"+in.opcode);
        }        	
        switch (in.opcode) {
            case "add":
                registers[in.rd]=in.result;
                break;
            case "sub":
                registers[in.rd]=in.result;
                break;
            case "mul":
                registers[in.rd]=in.result;
                break;
            case "mv":
                registers[in.rd]=in.result;
                break;
            case "addi":
                registers[in.rd]=in.result;
                break;
            case "muli":
                registers[in.rd]=in.result;
                break;
            case "rem":
                registers[in.rd]=in.result;
                break;
            case "beq":
                //pass for all conditional jumps
                break;
            case "lw":
                registers[in.rd]=in.result;
                break;
            case "li":
                registers[in.rd]=in.result;
                break;
            case "jal":
                registers[in.rd]=in.result;
                break;
			case "and":
				registers[in.rd]=in.result;
				break;
			case "or":
				registers[in.rd]=in.result;
				break;
			case "xor":
				registers[in.rd]=in.result;
				break;
			case "andi":
				registers[in.rd]=in.result;
				break;
			case "ori":
				registers[in.rd]=in.result;
				break;
			case "xori":
				registers[in.rd]=in.result;
				break;
			case "la":
				registers[in.rd]=in.result;
				break;
			case "jalr":
				registers[in.rd]=in.result;
				break;
            default:
                break;
        }
        // hardwiring x0 to 0.
        if(registers[0]!=0){
            registers[0]=0;
        }  
        if(this.coreID==0) {
        	in.WB_done_core0=true;
        }
        if(this.coreID==1) {
        	in.WB_done_core1=true;
        }
        if(this.coreID==2) {
        	in.WB_done_core2=true;
        }
        if(this.coreID==3) {
        	in.WB_done_core3=true;
        } 
    }
    
    public int hazardDetectorUtil(LinkedList<InstructionState>pipelineQueue,boolean isPipelineForwardingEnabled,int i) {
    	InstructionState curr=new InstructionState();  // fetching the instruction that is currently going to ID/RF
    	// fetching the previous three instructions to compare and check for dependencies
    	InstructionState prev1=new InstructionState();  
    	InstructionState prev2=new InstructionState();
    	InstructionState prev3=new InstructionState();
//    	if(this.coreID==0) {
//    		System.out.println("Op code for curr:"+curr.opcode);
//        	System.out.println("Op code for prev1:"+prev1.opcode);
//        	System.out.println("Op code for prev2:"+prev2.opcode);
//        	System.out.println("Op code for prev3:"+prev3.opcode);
//    	}
        if(!isPipelineForwardingEnabled){
            curr=pipelineQueue.get(3);
            prev1=pipelineQueue.get(2);
            prev2=pipelineQueue.get(1);
            prev3=pipelineQueue.get(0);
        }
        if(isPipelineForwardingEnabled){
            return hazardDetectorWithPipelineForwadingUtil(pipelineQueue,i);
        }
        else{
            return hazardDetector(curr,prev1,prev2,prev3);  // returning the computed data stalls
        }
    }
    
    public int hazardDetector(InstructionState curr, InstructionState prev1,InstructionState prev2,InstructionState prev3) {
    	// Don't do anything if current is dummy
    	if(curr.isDummy) {
    		return 0;
    	}

    	// checking for ecall seperately as it has no rd, rs1 or rs2 but exhibits RAW dependency
    	if(curr.opcode.equals("ecall")) {
    		if(!prev1.isDummy && prev1.rd!=-1) {
    			if(prev1.rd==10 || prev1.rd==17) {
    				if(this.coreID==0) {
    					curr.IDRF_done_core0=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once0=true;
    				}
    				if(this.coreID==1) {
    					curr.IDRF_done_core1=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once1=true;
    				}
    				if(this.coreID==2) {
    					curr.IDRF_done_core2=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once2=true;
    				}
    				if(this.coreID==3) {
    					curr.IDRF_done_core3=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once3=true;
    				}
        			return 3-1;
        		}
    		}
    		
    		if(!prev2.isDummy && prev2.rd!=-1) {
    			if(prev2.rd==10 || prev2.rd==17) {
    				if(this.coreID==0) {
    					curr.IDRF_done_core0=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once0=true;
    				}
    				if(this.coreID==1) {
    					curr.IDRF_done_core1=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once1=true;
    				}
    				if(this.coreID==2) {
    					curr.IDRF_done_core2=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once2=true;
    				}
    				if(this.coreID==3) {
    					curr.IDRF_done_core3=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once3=true;
    				} 
        			return 2-1;
        		}
    		}
    		
    		if(!prev3.isDummy && prev3.rd!=-1) {
    			if(prev3.rd==10 || prev3.rd==17) {
    				if(this.coreID==0) {
    					curr.IDRF_done_core0=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once0=true;
    				}
    				if(this.coreID==1) {
    					curr.IDRF_done_core1=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once1=true;
    				}
    				if(this.coreID==2) {
    					curr.IDRF_done_core2=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once2=true;
    				}
    				if(this.coreID==3) {
    					curr.IDRF_done_core3=false;  // perform the ID/RF again when stalls are found
    					curr.IDRF_done_once3=true;
    				}
        			return 1-1;
        		}
    		}
    		
    	}
    	
    	if(!prev1.isDummy && prev1.rd!=-1) {
    		if(curr.rs1==prev1.rd || curr.rs2==prev1.rd) {
    			if(this.coreID==0) {
					curr.IDRF_done_core0=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once0=true;
				}
				if(this.coreID==1) {
					curr.IDRF_done_core1=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once1=true;
				}
				if(this.coreID==2) {
					curr.IDRF_done_core2=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once2=true;
				}
				if(this.coreID==3) {
					curr.IDRF_done_core3=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once3=true;
				}
    			return 3-1;  // this indicates that there is a dependency with immediate previous instruction that lead to three stalls.  
    		}
    	}
    	if(!prev2.isDummy && prev2.rd!=-1) {
    		if(curr.rs1==prev2.rd || curr.rs2==prev2.rd) {
    			if(this.coreID==0) {
					curr.IDRF_done_core0=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once0=true;
				}
				if(this.coreID==1) {
					curr.IDRF_done_core1=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once1=true;
				}
				if(this.coreID==2) {
					curr.IDRF_done_core2=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once2=true;
				}
				if(this.coreID==3) {
					curr.IDRF_done_core3=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once3=true;
				}
    			return 2-1; // dependency with second previous instruction resulting in only two stalls
    		} 
    	}
    	if(!prev3.isDummy && prev3.rd!=-1) {
    		if(curr.rs1==prev3.rd || curr.rs2==prev3.rd) {
    			if(this.coreID==0) {
					curr.IDRF_done_core0=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once0=true;
				}
				if(this.coreID==1) {
					curr.IDRF_done_core1=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once1=true;
				}
				if(this.coreID==2) {
					curr.IDRF_done_core2=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once2=true;
				}
				if(this.coreID==3) {
					curr.IDRF_done_core3=false;  // perform the ID/RF again when stalls are found
					curr.IDRF_done_once3=true;
				}
    			return 1-1; // dependency with third prev instruction resulting in only one stall
    		}
    	}
    	return 0;
    }
    public int hazardDetectorWithPipelineForwadingUtil(LinkedList<InstructionState>pipelineQueue,int i){
        InstructionState curr=new InstructionState();
        InstructionState prev1=new InstructionState();
        InstructionState prev2=new InstructionState();
        InstructionState prev3=new InstructionState();
        if(i==1){
            curr=pipelineQueue.get(i);
            prev1=pipelineQueue.get(i-1);
        }
        else if(i==2){
            curr=pipelineQueue.get(i);
            prev1=pipelineQueue.get(i-1);
            prev2=pipelineQueue.get(i-2);
        }
        else if(i==3){
            curr=pipelineQueue.get(i);
            prev1=pipelineQueue.get(i-1);
            prev2=pipelineQueue.get(i-2);
            prev3=pipelineQueue.get(i-3);
        }
        return hazardDetectorWithPipelineForwading(curr, prev1, prev2, prev3);
    }
    public int hazardDetectorWithPipelineForwading(InstructionState curr, InstructionState prev1,InstructionState prev2,InstructionState prev3){
    	// Don't do anything if current is dummy
    	if(curr.isDummy) {
    		return 0;
    	}
        int stall=0;
        boolean isStall;
    	System.out.println("**** IS dummy: "+curr.isDummy + " and pc val "+curr.pc_val+ " is ID done "+ curr.IDRF_done_core0+ " and IF done "+curr.IF_done_core0+" and EX done "+curr.EX_done_core0+ " and MEM done "+curr.MEM_done_core0);// for debugging
        
        // System.out.println("Printing the pipeline ");
        // for(int i=0;i<pipeLineQueue.size();i++){
        //     System.out.print(pipeLineQueue.get(i).pc_val+" ");
        // }
        // System.out.println();

        // checking for ecall seperately as it has no rd, rs1 or rs2 but exhibits RAW dependency
        
    	if(curr.opcode.equals("ecall")) {
            isStall=true;
    		if(!prev1.isDummy && prev1.rd!=-1) {
    			if(prev1.rd==10 || prev1.rd==17) {
                    if(prev1.rd==10 && curr.pipeline_reg[0]==null){
                        curr.pipeline_reg[0]=prev1.result;
                        curr.isfowarded=true;
                        isStall=false;
                        // return 0;
                    }
                    else if(prev1.rd==17 && curr.pipeline_reg[1]==null){
                        curr.pipeline_reg[1]=prev1.result;
                        curr.isfowarded=true;
                        isStall=false;
                        // return 0;
                    }
                    else if(curr.isfowarded){
                        isStall=false;
                        // return 0;
                    }
        			// return 1;
                    if(isStall){
                        stall++;
                    }
        		}
    		}
    		
    		if(!prev2.isDummy && prev2.rd!=-1) {
                isStall=true;
    			if(prev2.rd==10 || prev2.rd==17) {
                    if(prev2.rd==10 && curr.pipeline_reg[0]==null){
                        curr.pipeline_reg[0]=prev2.result;
                        curr.isfowarded=true;
                        // return 0;
                        isStall=false;
                    }
                    else if(prev2.rd==17 && curr.pipeline_reg[1]==null){
                        curr.pipeline_reg[1]=prev2.result;
                        curr.isfowarded=true;
                        // return 0;
                        isStall=false;
                    }
                    else if(curr.isfowarded){
                        // return 0;
                        isStall=false;
                    }
        			// return 1;
                    if(isStall){
                        stall++;
                    }
        		}
    		}
    		
    		if(!prev3.isDummy && prev3.rd!=-1) {
                isStall=true;
    			if(prev3.rd==10 || prev3.rd==17) {
                    if(prev3.rd==10 && curr.pipeline_reg[0]==null){
                        curr.pipeline_reg[0]=prev3.result;
                        curr.isfowarded=true;
                        // return 0;
                        isStall=false;
                    }
                    else if(prev3.rd==17 && curr.pipeline_reg[1]==null){
                        curr.pipeline_reg[1]=prev3.result;
                        curr.isfowarded=true;
                        // return 0;
                        isStall=false;
                    }
                    else if(curr.isfowarded){
                        // return 0;
                        isStall=false;
                    }
        			// return 1;
                    if(isStall){
                        stall++;
                    }
        		}
    		}
    		
    	}
    	
    	if(!prev1.isDummy && prev1.rd!=-1) {
            isStall=true;
    		if(curr.rs1==prev1.rd || curr.rs2==prev1.rd) {
    			if(prev1.result!=null && curr.rs1==prev1.rd && curr.rs2==prev1.rd && curr.pipeline_reg[0]==null && curr.pipeline_reg[1]==null){
                    curr.pipeline_reg[0]=prev1.result;
                    curr.pipeline_reg[1]=prev1.result;
                    curr.isfowarded=true;
                    // return 0;
                    isStall=false;
                }
                else if(prev1.result!=null && curr.rs1==prev1.rd && curr.pipeline_reg[0]==null){
                    System.out.println("For the instruction :"+curr.opcode+" with pc value "+curr.pc_val+" the forwarding that is going to happen is "+prev1.result);
                    curr.pipeline_reg[0]=prev1.result;
                    curr.isfowarded=true;
                    // return 0;
                    isStall=false;
                }
                else if(prev1.result!=null && curr.rs2==prev1.rd && curr.pipeline_reg[1]==null){
                    System.out.println("For the instruction :"+curr.opcode+" with pc value "+curr.pc_val+" the forwarding that is going to happen is "+prev1.result);
                    curr.pipeline_reg[1]=prev1.result;
                    curr.isfowarded=true;
                    // return 0;
                    isStall=false;
                }
                else if(curr.isfowarded){
                    // return 0;
                    isStall=false;
                }
                if(isStall){
                    stall++;
                }
    			// return 1;  // this indicates that there is a dependency with immediate previous instruction that lead to one stall with forwarding.  
    		}
    	}
    	if(!prev2.isDummy && prev2.rd!=-1) {
            isStall=true;
    		if(curr.rs1==prev2.rd || curr.rs2==prev2.rd) {
    			if(prev2.result!=null && curr.rs1==prev2.rd && curr.rs2==prev2.rd && curr.pipeline_reg[0]==null && curr.pipeline_reg[1]==null){
                    curr.pipeline_reg[0]=prev2.result;
                    curr.pipeline_reg[1]=prev2.result;
                    curr.isfowarded=true;
                    // return 0;
                    isStall=false;
                }
                else if(prev2.result!=null && curr.rs1==prev2.rd && curr.pipeline_reg[0]==null){
                    System.out.println("2.For the instruction :"+curr.opcode+" with pc value "+curr.pc_val+" the forwarding that is going to happen is "+prev1.result);
                    curr.pipeline_reg[0]=prev2.result;
                    curr.isfowarded=true;
                    // return 0;
                    isStall=false;
                }
                else if(prev2.result!=null && curr.rs2==prev2.rd && curr.pipeline_reg[1]==null){
                    System.out.println("2.For the instruction :"+curr.opcode+" with pc value "+curr.pc_val+" the forwarding that is going to happen is "+prev1.result);
                    curr.pipeline_reg[1]=prev2.result;
                    curr.isfowarded=true;
                    // return 0;
                    isStall=false;
                }
                else if(curr.isfowarded){
                    // return 0;
                    isStall=false;
                }
                if(isStall){
                    stall++;
                }
    			// return 1;  // this indicates that there is a dependency with immediate previous instruction that lead to one stall with forwarding.  
    		}
    	}
    	if(!prev3.isDummy && prev3.rd!=-1) {
            isStall=true;
    		if(curr.rs1==prev3.rd || curr.rs2==prev3.rd) {
    			if(prev3.result!=null && curr.rs1==prev3.rd && curr.rs2==prev3.rd && curr.pipeline_reg[0]==null && curr.pipeline_reg[1]==null){
                    curr.pipeline_reg[0]=prev3.result;
                    curr.pipeline_reg[1]=prev3.result;
                    curr.isfowarded=true;
                    // return 0;
                    isStall=false;
                }
                else if(prev3.result!=null && curr.rs1==prev3.rd && curr.pipeline_reg[0]==null){
                    curr.pipeline_reg[0]=prev3.result;
                    curr.isfowarded=true;
                    // return 0;
                    isStall=false;
                }
                else if(prev3.result!=null && curr.rs2==prev3.rd && curr.pipeline_reg[1]==null){
                    curr.pipeline_reg[1]=prev3.result;
                    curr.isfowarded=true;
                    // return 0;
                    isStall=false;
                }
                else if(curr.isfowarded){
                    // return 0;
                    isStall=false;
                }
                if(isStall){
                    stall++;
                }
    			// return 1;  // this indicates that there is a dependency with immediate previous instruction that lead to one stall with forwarding.  
    		}
    	}
    	return stall;
    }

	public int[] registers;
    public int pc;
    public int coreID;
    private String a_0=""; // variable used for loading the string to be printed using ecall
    public int cc; 
    public int controlStalls;
    public int totalStalls;
    public int latencyStalls;
    public InstructionState lastInstruction;
    public LinkedList<InstructionState> pipeLineQueue;
    public int[] instructionsExecuted;
}