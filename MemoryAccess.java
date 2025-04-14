
public class MemoryAccess {

	private int addr;
	private Cache_L1D L1Cache;
	private Cache_L2 L2Cache;
	private Memory mem;
	private int latency;

	public MemoryAccess(int addr,Cache_L1D L1Cache, Cache_L2 L2Cache,Memory mem){
		this.addr=addr;
		this.L1Cache=L1Cache;
		this.L2Cache=L2Cache;
		this.mem=mem;
	}

	public MemoryResult readData(int addr){
		MemoryResult res=null;
		res=L1Cache.readData(addr);
		if(res!=null){
			return res;
		}
		res=L1Cache.fillCacheL1(addr, L2Cache, mem);
		return res;
	}
	
}
