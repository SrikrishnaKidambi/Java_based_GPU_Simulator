public class Memory {
    public static int[] memory;
    public Memory(){
        memory=new int[1024];
    }
    public static void printMemory(){
        for(int i=0;i<1024;i++){
            System.out.print(memory[i]+" ");
        }
        System.out.println();
    }
}
