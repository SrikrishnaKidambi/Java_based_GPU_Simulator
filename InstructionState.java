
public class InstructionState {
	
	public InstructionState(String instruction) {
		this.instruction=instruction;
		rd=-1;
		rs1=-1;
		rs2=-1;
		isDummy=true;
	}
	public String instruction;
	public String opcode;
	public int rd;
	public int rs1;
	public int rs2;
	public String labelName;
	public int immediateVal;
	public boolean isDummy;
	public int result;
}
