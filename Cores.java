//importing necessary libraries
import java.util.*;

public class Cores{
    public int[] registers;
    public int pc;
    public int coreID;

    public Cores(int coreID){
        this.coreID=coreID;
        this.pc=0;
        this.registers=new int[32];
        registers[0]=0;
    }

    public void execute(String[] program,Map<String,Integer>labelMapping,Memory mem){
        
        String[] decodedInstruction = program[pc].split(" ");
        String opcode=decodedInstruction[0].toUpperCase();
        // System.out.println(opcode);
        int rd,rs1;
        int rs2;
        switch(opcode){
            case "ADD":
                //Ex: add x1 x2 x3
                rd= Integer.parseInt(decodedInstruction[1].substring(1));
                rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X'){
                    rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                } 
                registers[rd] = registers[rs1] + registers[rs2]; break;
            case "SUB":
                //Ex: sub x1 x2 x3
                rd= Integer.parseInt(decodedInstruction[1].substring(1));
                rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                if(decodedInstruction[3].charAt(0)=='X'){
                    rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                }
                else{
                    rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                } 
                registers[rd] = registers[rs1] - registers[rs2]; break;
            case "MUL":
                    //Ex: mul x1 x2 x3
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    } 
                    registers[rd] = registers[rs1] * registers[rs2]; break;
            case "MV":
                    //Ex: mv x1 x2
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    
                    System.out.println(rd+" "+rs1);
                    registers[rd] = registers[rs1] ;
                    break;
            case "ADDI":
                    //Ex: addi x1 x2 8
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    } 
                    registers[rd] = registers[rs1] + rs2; break;
            case "MULI":
                    //Ex: muli x1 x2 3
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    } 
                    registers[rd] = registers[rs1] * rs2; break;
            case "AND" :
                    //Ex: and x1 x2 x3
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    } 
                    registers[rd] = registers[rs1] & registers[rs2]; break;
            case "OR" :
                    //Ex: or x1 x2 x3
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    } 
                    registers[rd] = registers[rs1] | registers[rs2]; break;
            case "XOR" :
                    //Ex: xor x1 x2 x3
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    } 
                    registers[rd] = registers[rs1] ^ registers[rs2]; break;
            case "ANDI" :
                    //Ex: andi x1 x2 8
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    } 
                    registers[rd] = registers[rs1] & rs2; break;
            case "ORI" :
                    //Ex: ori x1 x2 3
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    } 
                    registers[rd] = registers[rs1] | rs2; break;
            case "XORI" :
                    //Ex: xori x1 x2 3
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    }
                    registers[rd] = registers[rs1] ^ rs2; break;  
            case "BNE" :
                    //Ex: bne x1 x2 label
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    String labelName;
                    labelName=decodedInstruction[3];
                    if(registers[rd]!=registers[rs1]){
                        pc=labelMapping.get(labelName).intValue();
                    }
                    break;
            case "BEQ" :
                    //Ex: beq x1 x2 label
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    String labelName1;
                    labelName1=decodedInstruction[3];
                    if(registers[rd]==registers[rs1]){
                        pc=labelMapping.get(labelName1).intValue();
                    }
                    break;
            case "LW":
                    //Ex: lw x1 8(x2) where 8 is the offset/immediate value and x2 is the base register
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
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
                    int immediate=Integer.parseInt(decodedInstruction[2].substring(0,paramStart));
                    if(immediate<-2048 && immediate>2047){
                        System.out.println("The immediate value can only be between -2048 and 2047");
                        System.exit(0);
                        break;
                    }
                    immediate=immediate/4;
                    rs1=Integer.parseInt(decodedInstruction[2].substring(paramStart+2,paramEnd));
                    if(registers[rs1]+immediate>1024 || registers[rs1]+immediate<0){
                        System.out.println("The memory address is out of bounds");
                        System.exit(0);
                        break;
                    }
                    registers[rd]=Memory.memory[registers[rs1]+immediate];
                    break;
            case "LI":
                    //Ex: li x1 8
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    registers[rd]=Integer.parseInt(decodedInstruction[2]);
                    break;
            case "JAL":
                    //Ex: jal x1 label
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    registers[rd]=pc+1;
                    String labelName2;
                    labelName2=decodedInstruction[2];
                    System.out.println("label: "+labelName2);
                    pc=labelMapping.get(labelName2).intValue();
                    break;
            case "J":
                    //Ex: j label which is equivalent to jal x0 label
                    String labelName3;
                    labelName3=decodedInstruction[1];
                    pc=labelMapping.get(labelName3).intValue();
                    break;
            case "LA":
                    //Ex: la x1 8 where 8 is memory location in the memory array
                    //can extend in future if we add two segments text and data
                    //this is load adress instruction
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    registers[rd]=Integer.parseInt(decodedInstruction[2]);
                    break;
            default:
        }

        //hardwiring x0 to 0.
        if(registers[0]!=0){
            registers[0]=0;
        }

        pc++;
        System.out.println("The current program counter value is "+pc);
    }
}