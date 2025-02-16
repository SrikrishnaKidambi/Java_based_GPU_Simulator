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
        		"la x16 0",          // 0  - Load the base address (index 0) of the array into x16
        		"li x31 1",          // 1  - Set x31 to 1 (since the simulator increments by 1 for 4-byte steps)
        		"jal x1 bubbleSort", // 2  - Jump and link to bubbleSort function (save return address in x1)
         		"j Over",
        		"Swap:",             // 3  - Label for swapping two elements
        		"mv x22 x26",        // 4  - Move value of x26 into x22 (temporary storage)
        		"mv x26 x25",        // 5  - Move value of x25 into x26 (swap step)
        		"mv x25 x22",        // 6  - Move stored value from x22 into x25 (swap complete)
        		"sw x25 0(x20)",     // 7  - Store updated value of x25 at memory location x20
        		"sw x26 0(x21)",     // 8  - Store updated value of x26 at memory location x21
        		"bubbleSort:",       // 9  - Label for the bubble sort function
        		"addi x10 x0 0",     // 10 - Initialize x10 (outer loop index) to 0
        		"addi x11 x0 0",     // 11 - Initialize x11 (inner loop index) to 0
        		"addi x12 x0 4",     // 12 - Set x12 to 4 (array size)
        		"LoopOuter:",        // 13 - Label for the outer loop
        		"addi x14 x12 -1",   // 14 - Compute n-1 and store in x14
        		"bge x10 x14 Exit",  // 15 - If x10 >= n-1, exit sorting
        		"LoopInner:",        // 16 - Label for the inner loop
        		"sub x13 x12 x10",   // 17 - Compute n - x10
        		"addi x13 x13 -1",   // 18 - Compute (n - x10 - 1), last index for this pass
        		"bge x11 x13 Incrementer", // 19 - If x11 >= (n-x10-1), increment outer loop index
        		"mul x19 x11 x31",   // 20 - Compute offset (index * 1 since x31 = 1 in the simulator)
        		"add x20 x16 x19",   // 21 - Compute memory address for arr[x11]
        		"lw x25 0(x20)",     // 22 - Load arr[x11] into x25
        		"addi x21 x20 1",    // 23 - Compute address of arr[x11+1]
        		"lw x26 0(x21)",     // 24 - Load arr[x11+1] into x26
        		"blt x26 x25 Swap",  // 25 - If arr[x11+1] < arr[x11], swap
        		"addi x11 x11 1",    // 26 - Increment inner loop index
        		"j LoopInner",       // 27 - Jump to LoopInner
        		"Incrementer:",      // 28 - Label to increment outer loop index
        		"addi x10 x10 1",    // 29 - Increment outer loop index
        		"j LoopOuter",       // 30 - Jump to LoopOuter
        		"Exit:",             // 31 - Label for exit
        		"jr x1",             // 32 - Return to caller
        		"addi x29 x0 0",     // 33 - Reset x29 to 0 (not used in sorting)
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
