import java.util.Map;

public class Core {
    public int[] registers;
    public int pc;
    public int coreID;
    private String a_0=""; // variable used for loading the string to be printed using ecall
    public int cc;

    public Core(int coreID){
        this.coreID=coreID;
        this.pc=0;
        this.registers=new int[32];
        registers[0]=0;
        this.cc=0;
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
            case "beq":
                break;
            case "bge":
                break;
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
}
