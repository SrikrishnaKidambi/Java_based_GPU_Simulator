class ScratchPadMemory{
    private Integer[] spmem;
    private int accessLatency;
    private int spmSize;

    public ScratchPadMemory(int accessLatency,int spmSize){
        this.accessLatency=accessLatency;
        this.spmSize=spmSize;
        spmem=new Integer[spmSize];
    }

    public MemoryResult writeData(int addr,int data){

        if(addr>spmSize || addr<0){
            System.out.println("The address requested is out of bounds of the spm memory");
            System.exit(0);
        }
        spmem[addr] = data;
        MemoryResult res=new MemoryResult(this.accessLatency, data);
        return res;
    }

    public MemoryResult readData(int addr){

        if(addr>spmSize || addr<0){
            System.out.println("The address requested is out of bounds of the spm memory");
            System.exit(0);
        }
        int dataRead=spmem[addr];
        MemoryResult res=new MemoryResult(this.accessLatency, dataRead);
        return res;
    }

    public void printScratchPad(){
        System.out.println("Printing the ScratchPad memory: ");
        for(int i=0;i<spmSize;i+=4){
            System.out.print(spmem[i]+" ");
        }
        System.out.println();
    }
}