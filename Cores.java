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

  private String instructionParser(String instruction) {
	  int hashSymbolIdx=instruction.indexOf("#");
	  if(hashSymbolIdx!=-1) {
		  if(hashSymbolIdx>=0 && instruction.charAt(hashSymbolIdx-1)!=' ') {
			  throw new IllegalArgumentException("Invalid comment. Space is missing after the instruction");
		  }
		  return instruction.substring(0,hashSymbolIdx).trim();
	  }
	  return instruction.trim();
  }
  
  public void execute(String[] program,Map<String,Integer>labelMapping,Memory mem){
	  
	  String instruction=program[pc];
	  String parsedInstruction = null;
	  try {
		  parsedInstruction=instructionParser(instruction);
	  }catch(IllegalArgumentException e) {
		  System.err.println("Error occured is:"+e.getMessage());
	  }
      
      String[] decodedInstruction = parsedInstruction.trim().replace(",","").split("\\s+");  //neglecting the commas that are put between registers.
      String opcode=decodedInstruction[0].toUpperCase();
      // System.out.println(opcode);
      int rd,rs1;
      int rs2;
      switch(opcode){
          case "ADD":
              //Ex: add x1 x2 x3
              rd= Integer.parseInt(decodedInstruction[1].substring(1));
              rs1=Integer.parseInt(decodedInstruction[2].substring(1));
              if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
              if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
//                  System.out.print(rd + rs1);
                  registers[rd] = registers[rs1]; 
                  break;
          case "ADDI":
                  //Ex: addi x1 x2 8
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
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
          case "BLT":
                  //Ex: blt x1 x2 label
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  String labelName4;
                  labelName4=decodedInstruction[3];
                  if(registers[rd]<registers[rs1]){
                      pc=labelMapping.get(labelName4).intValue();
                  }
                  break;
          case "BGE":
                  //Ex: bge x1 x2 label
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  String labelName5;
                  labelName5=decodedInstruction[3];
                  if(registers[rd]>=registers[rs1]){
                      pc=labelMapping.get(labelName5).intValue();
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
                  if(immediate<-512 || immediate>512){
                      System.out.println("The immediate value can only be between -512 and 512"); // as the total memory the core can access is 1024 bytes so using immediate I can go 512bytes up and 512 bytes down
                      System.exit(0);
                      break;
                  }
                  immediate=immediate/4;
                  rs1=Integer.parseInt(decodedInstruction[2].substring(paramStart+2,paramEnd));
//                  System.out.println("The memory requested is "+registers[rs1]+immediate);
                  if(registers[rs1]+immediate>=256 || registers[rs1]+immediate<0){
                      System.out.println("The memory address requested is not accessible by the core");
                      System.exit(0);
                      break;
                  }
                  registers[rd]=Memory.memory[registers[rs1]+immediate+256*this.coreID];
                  break;
          case "SW":
                  // syntax of instruction: sw x10 4(x5)
                  // this means that from the register x10 take the value and store it in the memory location of x5 with an offset of 4.
                  rs2=Integer.parseInt(decodedInstruction[1].substring(1));
                  String[] offsetAndRegBase=decodedInstruction[2].split("[()]");  //considering ( and ) as two seperate delimiters.
//                  System.out.println("The immediate value is "+offsetAndRegBase[0]);
//                  System.out.println("The index of the register that store the base address of the memory is "+offsetAndRegBase[1].substring(1));
                  int immediate_val=Integer.parseInt(offsetAndRegBase[0]);
                  int registerBaseAddressLoc=Integer.parseInt(offsetAndRegBase[1].substring(1));

                  if(immediate_val<-512 || immediate_val>512){
                      System.out.println("Immediate value cannot be less than -512 or greater than 512");
                      System.exit(0);
                  }
                  immediate_val=immediate_val/4;
                  if((registers[registerBaseAddressLoc]+immediate_val)>=256 || (registers[registerBaseAddressLoc]+immediate_val)<0){
                      System.out.println("Memory out of bounds");
                      System.exit(0);
                  }
//                  System.out.println("Storing the value:"+registers[rs2]);
                  Memory.memory[registers[registerBaseAddressLoc]+immediate_val+256*this.coreID]=registers[rs2];
                  break;
          case "LI":
                  //Ex: li x1 8
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  int addressVal=Integer.parseInt(decodedInstruction[2]);
                  if(addressVal>=256 || addressVal<0){
                    System.out.println("Cannot access the requested memory location");
                    System.exit(0);
                    break;
                  }
                  registers[rd]=addressVal;
                  break;
          case "JAL":
                  //Ex: jal x1 label
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  registers[rd]=pc+1;
                  String labelName2;
                  labelName2=decodedInstruction[2];
//                  System.out.println("Label: "+labelName2);
                  pc=labelMapping.get(labelName2).intValue();
                  break;
          case "JALR":
                  // syntax : jalr x1 x2 x0 -> store the value of pc+4 in x1 and jump to x2+x0
                  rd=Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                  registers[rd]=pc+1;
                  pc=registers[rs1]+registers[rs2]-1;
                  break;
          case "J":
                  //Ex: j label which is equivalent to jal x0 label
                  String labelName3;
                  labelName3=decodedInstruction[1];
//                  System.out.println("Label in instruction j:"+labelName3);
                  pc=labelMapping.get(labelName3).intValue();
                  break;
          case "JR":
                  //Ex: jr x2 -> this is equivalent to jalr x0 x2 x0
                  int jumpToVal=Integer.parseInt(decodedInstruction[1].substring(1));
                  pc=registers[jumpToVal]-1;
                  break;
          case "LA":
                  //Ex: la x1 8 where 8 is memory location in the memory array
                  //can extend in future if we add two segments text and data
                  //this is load adress instruction
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  int addressVal1=Integer.parseInt(decodedInstruction[2]);
                  if(addressVal1>=256 || addressVal1<0){
                    System.out.println("Cannot access the requested memory location");
                    System.exit(0);
                    break;
                  }
                  registers[rd]=addressVal1;
                  break;
          default: Simulator.isInstruction=false;
      }

      //hardwiring x0 to 0.
      if(registers[0]!=0){
          registers[0]=0;
      }
//      System.out.println("The current program counter value is "+pc);
//      for(int i=0;i<32;i++){
//        System.out.print(registers[i]+" ");
//      }
//      System.out.println();
      pc++;
  }
}