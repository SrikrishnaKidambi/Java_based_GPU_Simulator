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
    }

    public void execute(String[] program,LinkedList<InstructionState>pipeLineQueue,Memory mem,Map<String,Integer>labelMapping,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping,Map<String,Integer>latencies,Map<Integer,Integer> dataHazardsMapping){
        if(pipeLineQueue.size()>=1){
            InstructionState in1=pipeLineQueue.get(0);
            WB(in1);
        }
        if(pipeLineQueue.size()>=2){
            InstructionState in2=pipeLineQueue.get(1);
            MEM(in2, mem);
        }
        if(pipeLineQueue.size()>=3){
            if(this.coreID==0){
                System.out.println("Number of latency stalls :"+this.latencyStalls);
            }
            for(int i=0;i<latencyStalls;i++){
                pipeLineQueue.add(2+i, new InstructionState());
            }
            latencyStalls=0;  //making the number of stalls due to latency to zero
            InstructionState in3=pipeLineQueue.get(2);
            EX(in3, labelMapping, stringVariableMapping, nameVariableMapping);
        }
        if(pipeLineQueue.size()>=4){
            for(int i=0;i<dataHazardsMapping.get(pc==0?0:this.pc-1);i++){
                pipeLineQueue.add(3+i+1, new InstructionState());
            }
            totalStalls+=dataHazardsMapping.get(pc==0?0:this.pc-1);
            // System.out.println("The pc "+(pc-1)+" and data stalls before this are: "+);
            if(this.coreID==3){
                dataHazardsMapping.put(pc==0?0:this.pc-1, 0);
            }
            InstructionState in4=pipeLineQueue.get(3);
            ID_RF(pipeLineQueue,in4, labelMapping, stringVariableMapping, nameVariableMapping,latencies);
        }
        if(pipeLineQueue.size()>=5){
            InstructionState in5=pipeLineQueue.get(4);
            if(this.coreID==0){
                System.out.println("Number of control stalls:"+this.controlStalls);
            }
            
            if(this.controlStalls>0){
                in5.isDummy=true;
                this.controlStalls--;
            }
            if(this.pc==program.length && in5.IF_done!=4){
                if(in5.pc_val==8){
                    System.out.println("You are making last instruction as dummy here");
                }
                in5.isDummy=true;
            }
            if(this.pc==program.length){
                lastInstruction=in5;
            }
            if(this.pc==program.length-1){
                System.out.println("The instruction that is going to get executed is dummy or not:"+in5.isDummy);
            }
            IF(program, in5);
        }
        
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
    private void IF(String[] program,InstructionState in){
        if(in.isDummy || in==null || in.IF_done==4){
            return;
        }
        if(coreID==0){
            System.out.println("The value of pc in IF:"+this.pc+" for opcode:"+in.opcode);
        }
        in.instruction=program[pc];
        in.pc_val=pc;
        pc++;
        if(this.pc==program.length){
            lastInstruction=in;
            System.out.println("Fetched the last instruction successfully with pc value"+in.pc_val);
        }
        
        in.isDummy=false;
        in.IF_done++;
        return;
    }
    private void ID_RF(LinkedList<InstructionState>pipeLineQueue,InstructionState in,Map<String,Integer>labelMapping,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping,Map<String,Integer>latencies){
        if(in.isDummy || in==null || in.IDRF_done==4){
            if(in.pc_val==8){   // hardcoded currently please check
                System.out.println("The last instruction is treated as dummy");
            }
            return;
        }
        String instruction=in.instruction;
        String parsedInstruction = null;
        try {
            parsedInstruction=instructionParser(instruction);
        }catch(IllegalArgumentException e) {
            System.err.println("Error occured is:"+e.getMessage());
        }
        if(coreID==0){
            System.out.println("The value of pc in ID/RF:"+this.pc+" for opcode:"+in.opcode);
        }        
        String[] decodedInstruction = parsedInstruction.trim().replace(","," ").split("\\s+");  //neglecting the commas that are put between registers.
        in.opcode=decodedInstruction[0].trim();
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
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                }
                latency=latencies.get(in.opcode);
                latencyStalls+=latency-1;
                totalStalls+=latency-1;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "sub":
                //Ex: sub x1 x2 x3
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                }
                latency=latencies.get(in.opcode);
                latencyStalls+=latency-1;
                totalStalls+=latency-1;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "mul":
                //Ex: mul x1 x2 x3
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                }
                latency=latencies.get(in.opcode);
                latencyStalls+=latency-1;
                totalStalls+=latency-1;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "mv":
                //Ex: mv x1 x2
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                latency=latencies.get(in.opcode);
                latencyStalls+=latency-1;
                totalStalls+=latency-1;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "addi":
                //Ex: addi x1 x2 8
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                }
                latency=latencies.get(in.opcode);
                latencyStalls+=latency-1;
                totalStalls+=latency-1;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "muli":
                //Ex: muli x1 x2 3
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                }
                latency=latencies.get(in.opcode);
                latencyStalls+=latency-1;
                totalStalls+=latency-1;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "rem":
                //Ex: rem x1 x2 x3
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                }
                latency=latencies.get(in.opcode);
                latencyStalls+=latency-1;
                totalStalls+=latency-1;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+latencyStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
			case "and": 
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				}else{
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
				}
				break;
			case "or": 
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				}else{
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
				}
				break;
			case "xor": 
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				}else{
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
				}
				break;
			case "andi": 
				in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				}
				else{
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
				} 
				break;
			case "ori": 
				in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				}
				else{
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
				} 
				break;
			case "xori": 
				in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
				}
				else{
					in.rs2=Integer.parseInt(decodedInstruction[3].substring(0));
				} 
				break;
			case "bne":
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				in.labelName=decodedInstruction[3];
				if(registers[in.rd]!=registers[in.rs1]){
					pc=labelMapping.get(in.labelName).intValue();
				}
                this.controlStalls++;
                totalStalls++;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
				break;
			case "blt":
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				in.labelName=decodedInstruction[3];
				if(registers[in.rd]<registers[in.rs1]){
					pc=labelMapping.get(in.labelName).intValue();
				}
                this.controlStalls++;
                totalStalls++;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
				break;
			case "sw": 
				// syntax of instruction: sw x10 4(x5)
                // this means that from the register x10 take the value and store it in the memory location of x5 with an offset of 4.
				in.rs2=Integer.parseInt(decodedInstruction[1].substring(1));
				String[] offsetAndRegBase=decodedInstruction[2].split("[()]");
				in.immediateVal=Integer.parseInt(offsetAndRegBase[0]);
				in.rd=Integer.parseInt(offsetAndRegBase[1].substring(1));  //here the address of the register that has the base address of the memory location is being stored.
				if(in.immediateVal<-512 || in.immediateVal>512){
					System.out.println("Immediate value cannot be less than -512 or greater than 512");
					System.exit(0);
				}
				break;
			case "jalr":
				// syntax : jalr x1 x2 x0 -> store the value of pc+4 in x1 and jump to x2+x0
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				in.rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                this.controlStalls++;
                this.controlStalls++;
                totalStalls+=2;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
				break;
			case "jr" : 
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
                this.controlStalls++;
                this.controlStalls++;
                totalStalls+=2;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
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
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                in.labelName=decodedInstruction[3];
                if(registers[in.rd]>=registers[in.rs1]){
                    pc=labelMapping.get(in.labelName).intValue();
                }
                this.controlStalls++;
                totalStalls++;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
                break;
            case "beq":
                //Ex: beq x1 x2 label
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                in.labelName=decodedInstruction[3];
                if(registers[in.rd]==registers[in.rs1]){
                    pc=labelMapping.get(in.labelName).intValue();
                }
                this.controlStalls++;
                totalStalls++;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
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
                if(in.immediateVal<-512 || in.immediateVal>512){
                    System.out.println("The immediate value can only be between -512 and 512"); // as the total memory the core can access is 1024 bytes so using immediate I can go 512bytes up and 512 bytes down
                    System.exit(0);
                    break;
                }
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(paramStart+2,paramEnd));
                break;
            case "li":
                //Ex: li x1 8
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.immediateVal=Integer.parseInt(decodedInstruction[2]);
                if(in.immediateVal>=256 || in.immediateVal<0){
                  System.out.println("Cannot access the requested memory location");
                  System.exit(0);
                  break;
                }
                break;
            case "jal":
                //Ex: jal x1 label
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.labelName=decodedInstruction[2];
                // pipeLineQueue.add(new InstructionState());
                // pipeLineQueue.add(new InstructionState()); 
                this.controlStalls++;
                this.controlStalls++;
                totalStalls+=2;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);  
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);            
                break;
            case "j":
                //Ex: j label which is equivalent to jal x0 label
                in.labelName=decodedInstruction[1];
                this.controlStalls++;
                this.controlStalls++;
                totalStalls+=2;
                if(coreID==0)
                System.out.println("Number of stalls in "+in.opcode+" are "+controlStalls);
                System.out.println("Total number of stalls in "+in.opcode+" are "+totalStalls);
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
        in.IDRF_done++;
    }
    private void EX(InstructionState in,Map<String,Integer>labelMapping,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping){
        if(in.isDummy || in==null || in.EX_done==4){
            return;
        }
        if(coreID==0){
            System.out.println("The value of pc in EX:"+this.pc+" for opcode:"+in.opcode);
        }        
        switch (in.opcode) {
            case "add":
                in.result = registers[in.rs1] + registers[in.rs2];
                break;
            case "sub":
                in.result = registers[in.rs1] - registers[in.rs2];
                break;
            case "mul":
                in.result = registers[in.rs1] * registers[in.rs2];
                break;
            case "mv":
                in.result = registers[in.rs1];
                break;
            case "addi":
                in.result = registers[in.rs1] + in.rs2;
                break;
            case "muli":
                in.result = registers[in.rs1] * in.rs2;
                break;
            case "rem":
                in.result = registers[in.rs1] % registers[in.rs2];
                break;
            case "lw":
                if(registers[in.rs1]+in.immediateVal+this.coreID>=1024 || registers[in.rs1]+in.immediateVal+this.coreID<0){
                    System.out.println("The memory address requested is not accessible by the core");
                    System.exit(0);
                    break;
                }
                in.result=registers[in.rs1]+in.immediateVal+this.coreID;
                break;
            case "li":
                in.result=in.immediateVal;
                break;
            case "jal":
                in.result=pc;
                pc=labelMapping.get(in.labelName).intValue();
                pc++;
                break;
            case "j":
                pc=labelMapping.get(in.labelName).intValue();
                pc++;
                break;
			case "jr":
				pc=registers[in.rd];
				break;
			case "and":
                in.result=registers[in.rs1] & registers[in.rs2];
				break;
			case "or":
                in.result=registers[in.rs1] | registers[in.rs2];
				break;
			case "xor":
                in.result=registers[in.rs1] ^ registers[in.rs2];
				break;
			case "andi":
                in.result=registers[in.rs1] & in.rs2;
				break;
			case "ori":
                in.result=registers[in.rs1] | in.rs2;
				break;
			case "xori":
                in.result=registers[in.rs1] ^ in.rs2;
				break;
			case "bne":
				// pass the other conditional branch instructions in the same way for execution phase
				break;
			case "sw":
				if((registers[in.rd]+in.immediateVal+this.coreID)>=1024 || (registers[in.rd]+in.immediateVal+this.coreID)<0){
					System.out.println("Memory out of bounds");
					System.exit(0);
				}
				in.result=registers[in.rd]+in.immediateVal+this.coreID;
				break;
			case "jalr": 
				in.result=pc;
				pc=registers[in.rs1]+registers[in.rs2];
				break;
			case "la":
				in.result=in.immediateVal;
				break;
			case "ecall":
                  // a0 -x10 and a7 - x17 please maintain these in the code
        	  	  int a7=registers[17];  // register x17 is used for identification of the data type of the value to be printed.
                //   System.out.println("The value of a7 is "+a7);
        	  	  switch(a7) {
        	  	  		case 1:
        	  	  			int a0=registers[10];
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
        in.EX_done++;
    }
    private void MEM(InstructionState in,Memory mem){
        if(in.isDummy || in==null || in.MEM_done==4){
            return;
        }
        if(coreID==0){
            System.out.println("The value of pc in MEM:"+this.pc+" for opcode:"+in.opcode);
        }		
        switch (in.opcode) {
            case "lw":
                in.result=mem.memory[in.result];
                break;
            case "sw":
                mem.memory[in.result]=registers[in.rs2];
            default:
                break;
        }
        in.MEM_done++;
    }
    private void WB(InstructionState in){
        if(in.isDummy || in==null || in.WB_done==4){
            return;
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
        //hardwiring x0 to 0.
        if(registers[0]!=0){
            registers[0]=0;
        }  
        in.WB_done++; 
    }
    
    // public void hazardDetector(String[] program, int curr_idx){
    //     String[] splitInstruction=program[curr_idx].trim().replace(","," ").split("\\s");
    //     String opcode=splitInstruction[0];
    //     switch (opcode) {
    //         case "add":
    //         case "sub":
    //         case "mul":
    //         case "rem":
    //         case "and":
    //         case "or":
    //         case "xor":
    //         case "addi":
    //         case "andi":
    //         case "ori":
    //         case "xori":
    //         case "mv":
    //         case "lw":
    //         case "jal":
    //         case "jalr":
    //         case "la":
    //         case "li":
    //             int rem_instructions=program.length-1-curr_idx;
    //             if(rem_instructions>=3){
    //                 String[] insN1=program[curr_idx+1].trim().replace(","," ").split("\\s");
    //                 String[] insN2=program[curr_idx+2].trim().replace(","," ").split("\\s");
    //                 String[] insN3=program[curr_idx+3].trim().replace(","," ").split("\\s");
    //                 String rdCurr=splitInstruction[1];
    //                 if(insN1[0].equals("j") || insN1[0].equals("jr") || insN2[0].equals("j") || insN2[0].equals("jr") || insN3[0].equals("j") || insN3[0].equals("jr") ){

    //                 }
    //                 else if(rdCurr.equals(insN1[2]) || (insN1.length==4 && rdCurr.equals(insN1[3]))){
    //                     dataStalls+=3;
    //                 }
    //                 else if(rdCurr.equals(insN2[2]) || (insN2.length==4 && rdCurr.equals(insN2[3]))){
    //                     dataStalls+=2;
    //                 }
    //                 else if(rdCurr.equals(insN3[2]) || (insN3.length==4 && rdCurr.equals(insN3[3]))){
    //                     dataStalls+=1;
    //                 }
    //             }
    //             else if(rem_instructions>=2){
    //                 String[] insN1=program[curr_idx+1].trim().replace(","," ").split("\\s");
    //                 String[] insN2=program[curr_idx+2].trim().replace(","," ").split("\\s");
    //                 String rdCurr=splitInstruction[1];
    //                 if(rdCurr.equals(insN1[2]) || (insN1.length==4 && rdCurr.equals(insN1[3]))){
    //                     dataStalls+=3;
    //                 }
    //                 else if(rdCurr.equals(insN2[2]) || (insN2.length==4 && rdCurr.equals(insN2[3]))){
    //                     dataStalls+=2;
    //                 }
    //             }
    //             else if(rem_instructions>=1){
    //                 String[] insN1=program[curr_idx+1].trim().replace(","," ").split("\\s");
    //                 String rdCurr=splitInstruction[1];
    //                 if(rdCurr.equals(insN1[2]) || (insN1.length==4 && rdCurr.equals(insN1[3]))){
    //                     dataStalls+=3;
    //                 }
    //             }
    //             break; 
    //         default:
    //             break;
    //     }
    // }

	public int[] registers;
    public int pc;
    public int coreID;
    private String a_0=""; // variable used for loading the string to be printed using ecall
    public int cc;
    public int controlStalls;
    public int totalStalls;
    public int latencyStalls;
    public int dataStalls;
    public InstructionState lastInstruction;
}
