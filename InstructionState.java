
public class InstructionState {
	
	public InstructionState() {
		rd=-1;
		rs1=-1;
		rs2=-1;
		isDummy=true;
		IF_done=0;
		IDRF_done=0;
		EX_done=0;
		MEM_done=0;
		WB_done=0;
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
	public int IF_done;
	public int IDRF_done;
	public int EX_done;
	public int MEM_done;
	public int WB_done;
}
