public class Memory {
    public static int[] memory;
    public static int addressCounter;
    public Memory(){
        memory=new int[1024];
        addressCounter=0; // base address is 0
    }
    public static void printMemory(){
        System.out.println("The memory of each core which is able to access 1kb of main memory is displayed below: ");
        System.out.println();
        System.out.println("Printing the memory for core 0:");
        for(int i=0;i<256;i++){
            System.out.print(memory[i]+" ");
            
        }
        System.out.println();
        System.out.println("Printing the memory for core 1:");
        for(int i=256;i<512;i++){
            System.out.print(memory[i]+" ");
        }
        System.out.println();
        System.out.println("Printing the memory for core 2:");
        for(int i=512;i<768;i++){
            System.out.print(memory[i]+" ");
        }
        System.out.println();
        System.out.println("Printing the memory for core 3:");
        for(int i=768;i<1024;i++){
            System.out.print(memory[i]+" ");
        }
        System.out.println();
    }
}
