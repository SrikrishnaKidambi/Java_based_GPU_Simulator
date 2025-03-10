import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import java.util.Iterator;

public class Test {
	
	public Test(Map<String,Integer> latencies){
		//initializing the latencies for the arithmetic instructions
		this.latencies=latencies;
	}
    public void RunSimulator() {
		// System.out.println("Enter the latencies for the arithmetic instructions: ");
		System.out.println(latencies.size());
		Iterator<Map.Entry<String, Integer>> iterator = latencies.entrySet().iterator();
		Scanner scanner = new Scanner(System.in);
		// while(iterator.hasNext()){
		// 	Map.Entry<String,Integer> entry=iterator.next();
		// 	System.out.println("Enter the latency for the instruction "+entry.getKey()+ " : ");
		// 	int latency=scanner.nextInt();
		// 	latencies.put(entry.getKey(), latency); 
		// }
		while(iterator.hasNext()){
			Map.Entry<String,Integer> entry =iterator.next();
			System.out.println("The latency for the ins "+entry.getKey()+" is "+entry.getValue());
		}
		System.out.println("Enter 1 to enable pipeline forwarding else enter 0: ");
		int input=scanner.nextInt();
		if(input==1){
			isPipelineForwardingEnabled=true;
		}
		else{
			isPipelineForwardingEnabled=false;
		}
        sim=new Simulator();
        readAssemblyFile();
        String[] textSegment=parseAssemblyCode();
        System.out.println("Printing parsed text segment");
        for(int i=0;i<textSegment.length;i++) {
        	System.out.println(i+" : "+textSegment[i]);
        }
        System.out.println("Memory before executing");
        mem.printMemory();	
        sim.initializeProgram(textSegment);
		printIntegerMapping();
		
        sim.runProgram(mem, stringVariableMapping, numberVariableMapping,latencies,isPipelineForwardingEnabled);
        System.out.println("Final result:");
        sim.printResult(latencies);      
        mem.printMemory();
    }
    
    
	public void readAssemblyFile(){
		String filePath= "program.asm";
		try{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			boolean textSegmentStarts=false;
			while((line = br.readLine())!=null){
				if(line.equals(".text")){
					textSegmentStarts=true;
				}
				if(line.equals(" ") || line.equals("")) {
					continue;
				}
				int colonIdx=-1;
				if(textSegmentStarts){
					colonIdx=line.indexOf(":");
				}			
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
	}
	
	
	public String[] parseAssemblyCode(){
		//data segment is restricted to be written only on top of the assembly code
		boolean isDataSegmentParsing=false;
		boolean isParsingTextSegment=false;
		
		for(String line: programArray){
			if(line.equals(".data")){
				isDataSegmentParsing=true;
			}
			if(isDataSegmentParsing){
				String[] parsedLine = line.split("#")[0].replace(",", " ").trim().split("\\s+");
				String dataType="";
				boolean hasVarName=false;
				if(dataTypeNames.contains(parsedLine[0])){
					dataType = parsedLine[0];
				}
				else{
					hasVarName=true;
					try{
						dataType = parsedLine[1];
					}
					catch(Exception e){
						System.out.println("Exception occured: ------------------------");
						System.out.println(parsedLine[0]);
					}
				}
				switch (dataType) {
					case ".word":
						int startIdx;
						if(hasVarName) {
							startIdx=2;
							numberVariableMapping.put(parsedLine[0].replace(":", ""), mem.addressCounter);
						}else {
							startIdx=1;
						}
						for(int i=startIdx ; i<parsedLine.length;i++){
							mem.memory[i*4+0-4*startIdx+mem.addressCounter] = Integer.parseInt(parsedLine[i].trim()); // core 0
							// mem.memory[i*4+1-4*startIdx+mem.addressCounter] = Integer.parseInt(parsedLine[i].trim()); // core 1
							// mem.memory[i*4+2-4*startIdx+mem.addressCounter] = Integer.parseInt(parsedLine[i].trim()); // core 2 
							// mem.memory[i*4+3-4*startIdx+mem.addressCounter] = Integer.parseInt(parsedLine[i].trim()); // core 3
						}
						mem.addressCounter+=4*(parsedLine.length-startIdx);
						if(mem.addressCounter>=1024) {
							System.out.println("Memory is being accessed out of bounds");
							System.exit(0);
						}
						break;
					case ".string":
						if(hasVarName){
							String originalString="";
							for(int i=2;i<parsedLine.length;i++){
								if(i==2){
									originalString+=parsedLine[i].substring(1)+" ";
								}
								else if(i==parsedLine.length-1){
									originalString+=parsedLine[i].substring(0,parsedLine[i].length()-1);
								}
								else{
									originalString+=parsedLine[i]+" ";
								}
							}
							System.out.print(originalString);
							stringVariableMapping.put(parsedLine[0].replace(":", ""), originalString.replace("\\n","\n").replace("\"",""));
						}
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
				if(!line.trim().isEmpty()){
					programCode.add(line);
				}
			}
			 
		}
		programCode.add("addi x0 x0 0");   // adding this extra line to address the issue of having a label at the end of the program code given as input
		String[] program=new String[programCode.size()];
		int i=0;
		for(String line:programCode) {
			program[i]=line.replace("\t", "").trim();
			i++;
		}
		// printStringMapping();
		// printIntegerMapping();   //debugging purpose
	
		return program;
	}
	public void printStringMapping(){
		for(Map.Entry<String,String> ele: stringVariableMapping.entrySet()){
			System.out.println("Var name: "+ele.getKey()+" and string: "+ele.getValue());
		}
	}
	public void printIntegerMapping() {
		System.out.println("Printing the values of indices mapped:");
		for(Map.Entry<String, Integer> ele:numberVariableMapping.entrySet()) {
			System.out.println("Var name: "+ele.getKey()+" and index: "+ele.getValue());
		}
	}
	public Map<String,Integer> latencies=new HashMap<>();
	public ArrayList<String> programArray= new ArrayList<>();
	public ArrayList<String> programCode=new ArrayList<>();
	public Set<String> dataTypeNames= new HashSet<>(Set.of(".word" , ".string",".data",".text"));
	public Map<String,String> stringVariableMapping = new HashMap<>();
	public Map<String,Integer> numberVariableMapping=new HashMap<>();
	public Memory mem=new Memory();
	public Simulator sim;
	public boolean isPipelineForwardingEnabled;
}
