public class Memory {
    public static int[] memory;
    public Memory(){
        memory=new int[1024];
    }
    public static void printMemory(){
        System.out.println("Printing the memory:");
        for(int i=0;i<4;i++){
            System.out.print(memory[i]+" ");
            
        }
        System.out.println();
    }
}
