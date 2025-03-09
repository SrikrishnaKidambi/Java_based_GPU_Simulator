
public class FetcherResult {
	
	public FetcherResult(InstructionState lastInstruction,int pc_val) {
		this.lastInstruction=lastInstruction;
		this.pc_val=pc_val;
	}
	
	public InstructionState lastInstruction;
	public int pc_val;
}
