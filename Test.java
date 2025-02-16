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
        
        String[] bubbleSortProgram={
        		"la x16 0",          // 0  
        		"li x31 1",          // 1 
        		"jal x1 bubbleSort", // 2 
         		"j Over",
        		"Swap:",             // 3
        		"mv x22 x26",        // 4
        		"mv x26 x25",        // 5
        		"mv x25 x22",        // 6 
        		"sw x25 0(x20)",     // 7 
        		"sw x26 0(x21)",     // 8 
        		"bubbleSort:",       // 9 
        		"addi x10 x0 0",     // 10
        		"addi x11 x0 0",     // 11 
        		"addi x12 x0 4",     // 12 
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
        System.out.println("Memory before executing");
        Memory.printMemory();
        sim.initializeProgram(bubbleSortProgram);
        sim.runProgram();
        System.out.println("Final result:");
        sim.printResult();
        
        Memory.printMemory();
    }
}
