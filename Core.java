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
				break;
            case "beq":
                break;
            case "bge":
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
    private void EX(){

    }
    private void MEM(){

    }
    private void WB(){

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
