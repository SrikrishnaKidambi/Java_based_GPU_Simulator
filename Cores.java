//importing necessary libraries
import java.util.*;

public class Cores{
  public int[] registers;
  public int pc;
  public int coreID;
  private String a_0=""; // variable used for loading the string to be printed using ecall
  public int cc;

  public Cores(int coreID){
      this.coreID=coreID;
      this.pc=0;
      this.registers=new int[32];
      registers[0]=0;
      this.cc=0;
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
  
  public void execute(String[] program,Map<String,Integer>labelMapping,Memory mem,Map<String,String>stringVariableMapping,Map<String,Integer>nameVariableMapping){
	  
	  String instruction=program[pc];
	  	  
	  String parsedInstruction = null;
	  try {
		  parsedInstruction=instructionParser(instruction);
	  }catch(IllegalArgumentException e) {
		  System.err.println("Error occured is:"+e.getMessage());
	  }
      
      String[] decodedInstruction = parsedInstruction.trim().replace(","," ").split("\\s+");  //neglecting the commas that are put between registers.
      String opcode=decodedInstruction[0].trim();
      // System.out.println(opcode);
      int rd,rs1;
      int rs2;
      switch(opcode){
          case "add":
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
          case "sub":
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
          case "mul":
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
          case "mv":
                  //Ex: mv x1 x2
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
//                  System.out.print(rd + rs1);
                  registers[rd] = registers[rs1]; 
                  break;
          case "addi":
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
          case "muli":
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
          case "and" :
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
          case "rem":
                  //Ex: rem x1 x2 x3
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  if(decodedInstruction[3].charAt(0)=='X' || decodedInstruction[3].charAt(0)=='x'){
                      rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                  }
                  else{
                      rs2=Integer.parseInt(decodedInstruction[3].substring(0));
                  } 
                  registers[rd] = registers[rs1] % registers[rs2]; break;
          case "or" :
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
          case "xor" :
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
          case "andi" :
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
          case "ori" :
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
          case "xori" :
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
          case "bne" :
                  //Ex: bne x1 x2 label
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  String labelName;
                  labelName=decodedInstruction[3];
                  if(registers[rd]!=registers[rs1]){
                      pc=labelMapping.get(labelName).intValue();
                  }
                  break;
          case "beq" :
                  //Ex: beq x1 x2 label
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  String labelName1;
                  labelName1=decodedInstruction[3];
                  if(registers[rd]==registers[rs1]){
                      pc=labelMapping.get(labelName1).intValue();
                  }
                  break;
          case "blt":
                  //Ex: blt x1 x2 label
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  String labelName4;
                  labelName4=decodedInstruction[3];
                  if(registers[rd]<registers[rs1]){
                      pc=labelMapping.get(labelName4).intValue();
                  }
                  break;
          case "bge":
                  //Ex: bge x1 x2 label
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  String labelName5;
                  labelName5=decodedInstruction[3];
                  if(registers[rd]>=registers[rs1]){
                      pc=labelMapping.get(labelName5).intValue();
                  }
                  break;
          case "lw":
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
                  rs1=Integer.parseInt(decodedInstruction[2].substring(paramStart+2,paramEnd));
//                  System.out.println("The memory requested is "+registers[rs1]+immediate);
                  if(registers[rs1]+immediate+this.coreID>=1024 || registers[rs1]+immediate+this.coreID<0){
                      System.out.println("The memory address requested is not accessible by the core");
                      System.exit(0);
                      break;
                  }
                  registers[rd]=mem.memory[registers[rs1]+immediate+this.coreID];
//                  Memory.printMemory();
                  break;
          case "sw":
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
                  if((registers[registerBaseAddressLoc]+immediate_val+this.coreID)>=1024 || (registers[registerBaseAddressLoc]+immediate_val+this.coreID)<0){
                      System.out.println("Memory out of bounds");
                      System.exit(0);
                  }
//                  System.out.println("Storing the value:"+registers[rs2]);
                  mem.memory[registers[registerBaseAddressLoc]+immediate_val+this.coreID]=registers[rs2];
                  break;
          case "li":
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
          case "jal":
                  //Ex: jal x1 label
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  registers[rd]=pc+1;
                  String labelName2;
                  labelName2=decodedInstruction[2];
//                  System.out.println("Label: "+labelName2);
                  pc=labelMapping.get(labelName2).intValue();
                  break;
          case "jalr":
                  // syntax : jalr x1 x2 x0 -> store the value of pc+4 in x1 and jump to x2+x0
                  rd=Integer.parseInt(decodedInstruction[1].substring(1));
                  rs1=Integer.parseInt(decodedInstruction[2].substring(1));
                  rs2=Integer.parseInt(decodedInstruction[3].substring(1));
                  registers[rd]=pc+1;
                  pc=registers[rs1]+registers[rs2]-1;
                  break;
          case "j":
                  //Ex: j label which is equivalent to jal x0 label
                  String labelName3;
                  labelName3=decodedInstruction[1];
//                  System.out.println("Label in instruction j:"+labelName3);
                  pc=labelMapping.get(labelName3).intValue();
                  break;
          case "jr":
                  //Ex: jr x2 -> this is equivalent to jalr x0 x2 x0
                  int jumpToVal=Integer.parseInt(decodedInstruction[1].substring(1));
                  pc=registers[jumpToVal]-1;
                  break;
          case "la":
                  //Ex: la x1 base contains the base address of the array.   
                  //this is load address instruction
                  rd= Integer.parseInt(decodedInstruction[1].substring(1));
                  String variableName=decodedInstruction[2];
                  if((decodedInstruction[1].equals("a0") || decodedInstruction[1].equals("x10") || decodedInstruction[1].equals("X10")) && stringVariableMapping.containsKey(variableName)) {
                	  a_0=stringVariableMapping.get(variableName);
                    //   System.out.println("The string that is printed due to ecall for variable name "+variableName+" is: " + a_0);
                	  break;
                  }
                  int addressVal1=nameVariableMapping.get(variableName);
//                  System.out.println("Loading the value: "+addressVal1);
                  registers[rd]=addressVal1;                  
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
          default: Simulator.isInstruction=false;
          if(!labelMapping.containsKey(opcode.trim().replace(":", "")) && !opcode.equals("")) {
        	  System.out.println(opcode.trim()+" is an invalid opcode");
        	  SimulatorGUI.console.append(opcode.trim()+" is an invalid opcode. So program execution is stopped!");
        	  throw new IllegalArgumentException(opcode.trim()+" is an invalid opcode");
          }
          
      }

      //hardwiring x0 to 0.
      if(registers[0]!=0){
          registers[0]=0;
      }
//      System.out.println("The value of x21 register for the core is:"+registers[20]+" "+this.coreID);
//      System.out.println("The current program counter value is "+pc);
//      System.out.println("The instruction that is being executed is:"+decodedInstruction[0]);
//      for(int i=0;i<32;i++){
//        System.out.print(registers[i]+" ");
//      }
//      System.out.println();
      pc++;
  }
}