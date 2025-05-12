public class Memory {
    public int[] memory;
    public int addressCounter;
    public int memoryAccessLatency;
    public Memory(int memoryAccessLatency){
        memory=new int[131076]; //now our memory is increased to 128KB 😊
        addressCounter=0; // base address is 0
        this.memoryAccessLatency=memoryAccessLatency;
    }
    public void printMemory(){
        System.out.println("The memory of the simulator:");
        // System.out.println("The memory of each core which is able to access 1kb of main memory is displayed below: ");
        // System.out.println();
        // System.out.println("Printing the memory for core 0:");
        // for(int i=0;i<1024;i+=4){
        //     System.out.print(memory[i]+" ");
            
        // }
        // System.out.println();
        // System.out.println("Printing the memory for core 1:");
        // for(int i=1;i<1024;i+=4){
        //     System.out.print(memory[i]+" ");
        // }
        // System.out.println();
        // System.out.println("Printing the memory for core 2:");
        // for(int i=2;i<1024;i+=4){
        //     System.out.print(memory[i]+" ");
        // }
        // System.out.println();
        // System.out.println("Printing the memory for core 3:");
        // for(int i=3;i<1024;i+=4){
        //     System.out.print(memory[i]+" ");
        // }
        for(int i=0;i<131076;i+=4){
            System.out.print(memory[i]+" ");
            // if(memory[i]==2){
            //     SimulatorGUI.console.append("The index at which we found 2 is:"+i);
            // }
        }
        System.out.println();
        String msg="\nDone printing the memory\n";
        System.out.print(msg);
        
    }
}
