public class MemoryResult {
    public int latency;
    public Integer result;

    public MemoryResult(int latency,Integer result){
    	System.out.println("Executed the memory result function");
        this.latency=latency;
        this.result=result;
    }
}
