
public class InstructionState {
	
	public InstructionState() {
		rd=-1;
		rs1=-1;
		rs2=-1;
		isDummy=true;
		pc_val=null;
		
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
		result=null;
		pipeline_reg=new Integer[8];
		pipeline_reg[0]=null; // this refer for rs1 content in general due to forwarding
		pipeline_reg[1]=null; // this refer for rs2 content in general due to forwarding

		isfowarded=false;
	}
	public Integer[] pipeline_reg;
	public String instruction;
	public String opcode;
	public int rd;
	public int rs1;
	public int rs2;
	public String labelName;
	public int immediateVal;
	public boolean isDummy;
	public Integer result;
	public int addressIdx;
	public boolean isfowarded;
	
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

	public Integer pc_val;
}
