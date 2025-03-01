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
                
                break;
            case "sub":
                break;
            case "mul":
                break;
            case "mv":
                break;
            case "addi":
                break;
            case "muli":
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
				
            default:
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
