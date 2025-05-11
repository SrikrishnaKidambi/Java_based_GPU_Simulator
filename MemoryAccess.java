
public class MemoryAccess {

	private Cache_L1D L1Cache;
	private Cache_L1I L1Cache_I;
	private Cache_L2 L2Cache;
	private Memory mem;


	public MemoryAccess(Cache_L1D L1Cache, Cache_L2 L2Cache,Cache_L1I L1Cache_I,Memory mem){
		this.L1Cache=L1Cache;
		this.L2Cache=L2Cache;
		this.L1Cache_I=L1Cache_I;
		this.mem=mem;
	}

	public MemoryResult readData(int addr){
		// L1Cache.accesses++;
		MemoryResult res=null;
		res=L1Cache.readData(addr);
		if(res!=null){
			return res;
		}
		// L1Cache.misses++;
		System.out.println("The address fetch while reading at "+addr+" is a miss in L1 cache");
		res=L1Cache.fillCacheL1(addr, L2Cache, mem);
		System.out.println("The memory result is returned "+res);
		return res;
	}
	
	public MemoryResult readInstruction(int addr) {
		// L1Cache_I.accesses++;
		MemoryResult res=null;
		res=L1Cache_I.readData(addr);
		if(res!=null) {
			return res;
		}
		// L1Cache_I.misses++;
		System.out.println("The address fetch while reading at "+addr+" is a miss in L1 cache");
		res=L1Cache_I.fillCacheL1(addr, L2Cache, mem);
		System.out.println("The memory result is returned"+res);
		return res;
	}

	public MemoryResult writeData(int addr,int updatedVal){
		// L1Cache.accesses++;
		MemoryResult res=null;
		res=L1Cache.writeData(addr, updatedVal);
		if(res!=null){
			return res;
		}
		// L1Cache.misses++;
		System.out.println("The address fetch while writing to "+addr+" is a miss in L1 cache");
		res=L1Cache.fillCacheL1(addr, L2Cache, mem);
		MemoryResult res1=L1Cache.writeData(addr, updatedVal);
		res.result=res1.result;
		return res;
	}
	

}
