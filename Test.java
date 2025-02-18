import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        // Cores core1= new Cores(1);
        // String[] program={"Add X1 X2 X3","ADDi X2 X3 100"};
        // core1.registers[3]=3;
        // core1.registers[2]=1;
        // for(int ele: core1.registers){
        //     System.out.print(ele+" ");
        // }
        // System.out.println();
        // core1.execute(program);
        // for(int ele: core1.registers){
        //     System.out.print(ele+" ");
        // }      
        // System.out.println();
        // core1.execute(program);
        // for(int ele: core1.registers){
        //     System.out.print(ele+" ");
        // }
        // System.out.println();
        Simulator sim=new Simulator();
        // String[] program={"Add X1 X2 X3","BNE X2 X3 Exit","ADDi X2 X3 100","Exit:","Mul X4 X3 X2"};
//         String[] program2 = {
//             "LI X1 10",
//             "LI X2 20",
//             "SW X1 20(X4)",
//             "Beq X1 X2 Label1",
//             "LI X3 30",
//             "Label1:",
//             "LA X4 100",
//             "LW X5 16(X4)",
//             "JAL X6 Function",
//             "J End",
//             "Function:",
//             "Add X7 X1 X2",
//             "End:"
//         };
        readAssemblyFile();
        String[] test={
        		"la x16, 0 #This is a comment",          // 0  
        		"li x31 1",          // 1 
        		"jal x1 bubbleSort", // 2 
         		"j Over",
        		"Swap:",             // 3
        		"	mv x22, x26",        // 4
        		"	mv x26 x25",        // 5
        		"	mv x25 x22",        // 6 
        		"sw x25 0(x20)",     // 7 
        		"sw x26 0(x21)", // 8 
        		"bubbleSort:",       // 9 
        		"addi x10 x0 0",     // 10
        		"addi x11 x0 0",     // 11 
        		"addi x12 x0 20",     // 12 
        		"",
        		"LoopOuter:",        // 13 
        		"addi x14 x12 -1",   // 14 
        		"bge x10 x14 Exit",  // 15 
        		"LoopInner:",        // 16
        		"sub x13 x12 x10",   // 17 
        		"addi x13 x13 -1",   // 18 
        		"bge x11 x13 Incrementer", // 19 
        		"mul x19 x11 x31",   // 20
        		"add x20 x16 x19",   // 21
        		"lw x25 0(x20)",     // 22 
        		"addi x21 x20 1",    // 23 
        		"lw x26 0(x21)",     // 24 
        		"blt x26 x25 Swap",  // 25 
        		"addi x11 x11 1",    // 26
        		"j LoopInner",       // 27 
        		"Incrementer:",      // 28 
        		"addi x10 x10 1",    // 29
        		"j LoopOuter",       // 30 
        		"Exit:",             // 31 
        		"jr x1",             // 32 
        		"addi x29 x0 0",     // 33
				"Over:",
        };
        String[] textSegment=parseAssemblyCode();
        if(textSegment.equals(test)) {
        	System.out.println("Pasring is done well");
        }else {
        	System.out.println("Parsing is not done well");
        	System.out.println("Printing parsed text segment");
        	for(int i=0;i<textSegment.length;i++) {
        		System.out.println(textSegment[i]);
        		if(textSegment[i].equals("")) {
        			System.out.println("Empty strings or spaces are present");
        		}
        	}
        	System.out.println();
        }
        System.out.println("Memory before executing");
        Memory.printMemory();
        sim.initializeProgram(textSegment);
        sim.runProgram();
        System.out.println("Final result:");
        sim.printResult();
        //comment for git integration
        
        Memory.printMemory();
    }
	public static void readAssemblyFile(){
		String filePath= "bubbleSort.asm";
		try{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			while((line = br.readLine())!=null){
				if(line.equals(" ") || line.equals("")) {
					continue;
				}
				int colonIdx=line.indexOf(":");
				if(colonIdx!=-1) {    // this indicates that label is present in the line
					programArray.add(line.substring(0,colonIdx+1));
					if(!line.substring(colonIdx+1).equals("")) {
						programArray.add(line.substring(colonIdx+1));
					}
					continue;
				}
				programArray.add(line.trim());
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		for(String instruction: programArray){
//			if(instruction==" ") {
//				System.out.println("Space included");
//			}
			System.out.println(instruction);
		}
		
	}
	public static String[] parseAssemblyCode(){
		//data segment is restricted to be written only on top of the assembly code
		boolean isDataSegmentParsing=false;
		boolean isParsingTextSegment=false;
		
		for(String line: programArray){
			if(line.equals(".data")){
				isDataSegmentParsing=true;
//				programArray.remove(line);
			}
			if(isDataSegmentParsing){
				String[] parsedLine = line.split("#")[0].replace(",", " ").trim().split("\\s+");
				String dataType = parsedLine[0];
				switch (dataType) {
					case ".word":
						for(int i=1 ; i<parsedLine.length;i++){
							Memory.memory[i*4+0-4+Memory.addressCounter] = Integer.parseInt(parsedLine[i].trim()); // core 0
							Memory.memory[i*4+1-4+Memory.addressCounter] = Integer.parseInt(parsedLine[i].trim()); // core 1
							Memory.memory[i*4+2-4+Memory.addressCounter] = Integer.parseInt(parsedLine[i].trim()); // core 2 
							Memory.memory[i*4+3-4+Memory.addressCounter] = Integer.parseInt(parsedLine[i].trim()); // core 3
						}
						Memory.addressCounter+=4*(parsedLine.length-1);
						break;
					default:
						break;
				}
//				programArray.remove(line);
			}
			if(line.equals(".text")){
				isDataSegmentParsing=false;
				isParsingTextSegment=true;
			}
			if(isParsingTextSegment && !isDataSegmentParsing && !line.equals(".text")) {
				programCode.add(line);
			}
		}
		System.out.println("The array list of text segment");
		for(String instruction: programCode){
//			if(instruction==" ") {
//				System.out.println("Space included");
//			}
			System.out.println(instruction);
		}
		String[] program=new String[programCode.size()];
		int i=0;
		for(String line:programCode) {
			program[i]=line;
			i++;
		}
		return program;
	}
	public static ArrayList<String> programArray= new ArrayList<>();
	public static ArrayList<String> programCode=new ArrayList<>();
}
