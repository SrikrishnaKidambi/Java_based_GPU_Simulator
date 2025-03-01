//importing necessary libraries
import java.util.*;

public class Core {
	public int[] registers;
	public int pc;
	public int coreID;
	private String a_0=""; // variable used for loading the string to be printed using ecall
	public int cc;

	public Core(int coreID) {
		this.coreID=coreID;
		this.pc=0;
		this.registers=new int[32];
		registers[0]=0;
		this.cc=0;
	}

	
	
}
