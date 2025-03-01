import java.util.Map;

public class Core {

    public Core(int coreID,Map<String,Integer>labelMapping,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping){
        this.coreID=coreID;
        this.pc=0;
        this.registers=new int[32];
        registers[0]=0;
        this.cc=0;
		this.labelMapping=labelMapping;
		this.stringVariableMapping=stringVariableMapping;
		this.nameVariableMapping=nameVariableMapping;
    }

    public void execute(String[] program,Map<String,Integer>labelMapping,Memory mem,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping){

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
    private InstructionState IF(String[] program){
        InstructionState in=new InstructionState(program[pc++]);
        in.isDummy=false;
        return in;
    }
    private void ID_RF(InstructionState in){
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
                break;
            case "mv":
                //Ex: mv x1 x2
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
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
				break;
			case "blt":
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
				in.labelName=decodedInstruction[3];
				if(registers[in.rd]<registers[in.rs1]){
					pc=labelMapping.get(in.labelName).intValue();
				}
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
				break;
			case "jr" : 
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				break;
			case "la" :
				in.rd=Integer.parseInt(decodedInstruction[1].substring(1));
				in.labelName=decodedInstruction[2];  // this indicates variable name
				in.immediateVal=nameVariableMapping.get(in.labelName);  // this indicates the value that has to be loaded into the register directly
				if((decodedInstruction[1].equals("a0") || decodedInstruction[1].equals("x10") || decodedInstruction[1].equals("X10")) && stringVariableMapping.containsKey(in.labelName)) {
					a_0=stringVariableMapping.get(in.labelName);
				  //   System.out.println("The string that is printed due to ecall for variable name "+variableName+" is: " + a_0);
					break;
				}
				break;
            case "bge":
                //Ex: bge x1 x2 label
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                in.labelName=decodedInstruction[3];
                if(registers[in.rd]>=registers[in.rs1]){
                    pc=labelMapping.get(in.labelName).intValue();
                }
                break;
            case "beq":
                //Ex: beq x1 x2 label
                in.rd= Integer.parseInt(decodedInstruction[1].substring(1));
                in.rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                in.labelName=decodedInstruction[3];
                if(registers[in.rd]==registers[in.rs1]){
                    pc=labelMapping.get(in.labelName).intValue();
                }
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
                break;
            case "j":
                //Ex: j label which is equivalent to jal x0 label
                in.labelName=decodedInstruction[1];
                break;
			case "ecall":
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
    }
    private void EX(InstructionState in){
        if(in.isDummy){
            return;
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
                registers[in.rd]=pc;
                pc=labelMapping.get(in.labelName).intValue();
            case "j":
                pc=labelMapping.get(in.labelName).intValue();
                pc++;
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
				registers[in.rd]=pc+1;
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
        	  	  			System.out.print(a0);
        	  	  			if(this.coreID==0) {
        	  	  				SimulatorGUI.console.append(a0+"");
        	  	  			}
        	  	  			break;
        	  	  		case 4:
                            // System.out.println("Printing as per request of mogambo");
        	  	  			System.out.print(a_0);
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
        //hardwiring x0 to 0.
        if(registers[0]!=0){
            registers[0]=0;
        }
    }
    private void MEM(InstructionState in,Memory mem){
        if(in.isDummy){
            return;
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
    }
    private void WB(InstructionState in){
        if(in.isDummy){
            return;
        }	
    }

	public int[] registers;
    public int pc;
    public int coreID;
    private String a_0=""; // variable used for loading the string to be printed using ecall
    public int cc;
	Map<String,Integer>labelMapping;
	Map<String,String>stringVariableMapping;
	Map<String,Integer>nameVariableMapping;

}
