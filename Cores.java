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
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    if(decodedInstruction[3].charAt(0)=='X'){
                        rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                    }
                    else{
                        rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                    } 
                    registers[rd] = registers[rs1] + registers[0]; break;
            case "ADDI":
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
                    rd= Integer.parseInt(decodedInstruction[1].substring(1));
                    rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                    String labelName;
                    labelName=decodedInstruction[3];
                    if(registers[rd]!=registers[rs1]){
                        pc=labelMapping.get(labelName).intValue();
                    }
        }

        //hardwiring x0 to 0.
        if(registers[0]!=0){
            registers[0]=0;
        }

        pc++;
        System.out.println("The current program counter value is "+pc);
    }
}