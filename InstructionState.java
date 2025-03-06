
public class InstructionState {
	
	public InstructionState() {
		rd=-1;
		rs1=-1;
		rs2=-1;
		isDummy=true;
		
		IF_done_core0=false;
		IDRF_done_core0=false;
		EX_done_core0=false;
		MEM_done_core0=false;
		WB_done_core0=false;
		
		IF_done_core1=false;
		IDRF_done_core1=false;
		EX_done_core1=false;
		MEM_done_core1=false;
		WB_done_core1=false;
		
		IF_done_core2=false;
		IDRF_done_core2=false;
		EX_done_core2=false;
		MEM_done_core2=false;
		WB_done_core2=false;
		
		IF_done_core3=false;
		IDRF_done_core3=false;
		EX_done_core3=false;
		MEM_done_core3=false;
		WB_done_core3=false;
		
		IDRF_done_once0=false;
		IDRF_done_once1=false;
		IDRF_done_once2=false;
		IDRF_done_once3=false;
		
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
	
	public boolean IF_done_core0;
	public boolean IDRF_done_core0;
	public boolean EX_done_core0;
	public boolean MEM_done_core0;
	public boolean WB_done_core0;
	
	public boolean IF_done_core1;
	public boolean IDRF_done_core1;
	public boolean EX_done_core1;
	public boolean MEM_done_core1;
	public boolean WB_done_core1;
	
	public boolean IF_done_core2;
	public boolean IDRF_done_core2;
	public boolean EX_done_core2;
	public boolean MEM_done_core2;
	public boolean WB_done_core2;
	
	public boolean IF_done_core3;
	public boolean IDRF_done_core3;
	public boolean EX_done_core3;
	public boolean MEM_done_core3;
	public boolean WB_done_core3;
	
	public boolean IDRF_done_once0;
	public boolean IDRF_done_once1;
	public boolean IDRF_done_once2;
	public boolean IDRF_done_once3;   // these variables hold the information that control stalls already got incremented so do not do again if the instruction is undergoing ID/RF

	public int pc_val;
}
