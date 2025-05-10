
public class FetcherResult {
	
	public FetcherResult(InstructionState lastInstruction,int pc_val,int memoryStalls,int stall) {
		this.lastInstruction=lastInstruction;
		this.pc_val=pc_val;
		this.memoryStalls=memoryStalls;
		this.stall=stall;
	}
	
	public InstructionState lastInstruction;
	public int pc_val;
	public int memoryStalls;
	public int stall;
}
