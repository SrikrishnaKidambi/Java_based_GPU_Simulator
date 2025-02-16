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
        String[] program2 = {
            "LI X1 10",
            "LI X2 20",
            "SW X1 20(X4)",
            "Beq X1 X2 Label1",
            "LI X3 30",
            "Label1:",
            "LA X4 100",
            "LW X5 16(X4)",
            "JAL X6 Function",
            "J End",
            "Function:",
            "mv x2 x3",
            "Add X7 X1 X2",
            "End:"
        };
        sim.initializeProgram(program2);
        sim.runProgram();
        sim.printResult();
        Memory.printMemory();
    }
}
