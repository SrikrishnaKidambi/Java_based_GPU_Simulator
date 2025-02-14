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

    public void execute(String[] program){
        String[] decodedInstruction = program[pc].split(" ");
        String opcode=decodedInstruction[0].toUpperCase();
        //System.out.println(opcode);
        int rd= Integer.parseInt(decodedInstruction[1].substring(1));
        int rs1=Integer.parseInt(decodedInstruction[2].substring(1));
        int rs2;
        if(decodedInstruction[3].charAt(0)=='X'){
            rs2=Integer.parseInt(decodedInstruction[3].substring(1));
        }
        else{
            rs2=Integer.parseInt(decodedInstruction[3].substring(0));
        }
        //System.out.println(rd+" "+rs1+" "+rs2);
        switch(opcode){
            case "ADD": registers[rd] = registers[rs1] + registers[rs2]; break;
            case "SUB": registers[rd] = registers[rs1] - registers[rs2]; break;
            case "MUL": registers[rd] = registers[rs1] * registers[rs2]; break;
            case "MV": registers[rd] = registers[rs1] + registers[0]; break;
            case "ADDI": registers[rd] = registers[rs1] + rs2; break;
            case "MULI": registers[rd] = registers[rs1] * rs2; break;
            case "AND" : registers[rd] = registers[rs1] & registers[rs2]; break;
            case "OR" : registers[rd] = registers[rs1] | registers[rs2]; break;
            case "XOR" : registers[rd] = registers[rs1] ^ registers[rs2]; break;
            case "ANDI" : registers[rd] = registers[rs1] & rs2; break;
            case "ORI" : registers[rd] = registers[rs1] | rs2; break;
            case "XORI" : registers[rd] = registers[rs1] ^ rs2; break;
        }
        if(registers[0]!=0){
            registers[0]=0;
        }
        pc++;
    }
}