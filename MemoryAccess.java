
public class MemoryAccess {

	private Cache_L1D L1Cache;
	private Cache_L2 L2Cache;
	private Memory mem;

	public MemoryAccess(Cache_L1D L1Cache, Cache_L2 L2Cache,Memory mem){
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
		System.out.println("The address fetch "+addr+" is a miss in L1 cache");
		res=L1Cache.fillCacheL1(addr, L2Cache, mem);
		System.out.println("The memory result is returned "+res);
		return res;
	}
	
}
