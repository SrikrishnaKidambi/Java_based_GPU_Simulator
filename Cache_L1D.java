import java.io.BufferedWriter;

public class Cache_L1D {
    public Integer[] tag;
    public Integer[] cache;
    public int associativity;
    public int blockSize;
    public int accessLatency;
    public int cacheSize;
    private int latencyExtra;
    private Integer[][] lru;
    private Integer[][] srrip;
    private boolean policy; //here 0 for lru and 1 for srrip
    private int maxRRPV=3;
    public int misses;
    public int accesses;
    public int id;
    public boolean[] dirty_arr;

    public Cache_L1D(int associativity,int blockSize,int cacheSize,int accessLatency,boolean whichPolicy,int ID){
        this.associativity=associativity;
        this.blockSize=blockSize;
        this.cacheSize=cacheSize;
        this.accessLatency=accessLatency;
        this.policy=whichPolicy;
        cache=new Integer[cacheSize];
        tag=new Integer[cacheSize/blockSize];
        int sets=cacheSize/(blockSize*associativity);
        lru=new Integer[sets][associativity];
        srrip=new Integer[sets][associativity];
        for(int i=0;i<sets;i++){
            for(int j=0;j<associativity;j++){
                lru[i][j]=j;
            }
        }
        for(int i=0;i<sets;i++){
            for(int j=0;j<associativity;j++){
                srrip[i][j]=maxRRPV;
            }
        }
        this.misses=0;
        this.accesses=0;
        this.id=ID;
        this.dirty_arr=new boolean[cacheSize/blockSize];
    }

    public void flushCache(Cache_L2 L2_Cache,Memory mem,int coreID){
        // SimulatorGUI.console.append("L1 cache for Core"+coreID+":\n");
        // for(int i=0;i<cache.length;i++){
        //     SimulatorGUI.console.append(cache[i]+" ");
        // }
        int idx=0;
        for(int i=0;i<tag.length;i++){
            if(i!=0 && i%associativity==0){
                idx++;
            }
            if(tag[i]!=null){
                int numSets=cacheSize/(blockSize*associativity);
                // int addr=tag[i]*numSets*blockSize+idx*blockSize;
                for(int j=i*blockSize;j<i*blockSize+blockSize;j+=4){
                    int addr=tag[i]*numSets*blockSize+idx*blockSize+j-i*blockSize;
                    
                    // if(cache[j]==null){
                    //     SimulatorGUI.console.append("The value of j at which it is null is:"+j+" and the value of the tag for this is "+tag[i]);
                    // }
                    mem.memory[addr]=cache[j];
                    L2_Cache.writeData(addr, mem, cache[j]);
                }
                if(dirty_arr[i]){
                    tag[i]=null;
                }
            }
            
        }
    }

    public MemoryResult readData(int addr){
        this.accesses++;
        // if(this.id==0){
        //     SimulatorGUI.console.append("Incrementing bro\n");
        // }
        int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
        int offset=addr%blockSize;
        int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        int tagBits=tagIdx/numSets;
        MemoryResult res=null;
        for(int i=idx*associativity;i<idx*associativity+associativity;i++){
            if(tag[i]!=null) {
            	if(tag[i]==tagBits){
                    res=new MemoryResult(this.accessLatency, cache[i*blockSize+offset]);
                    if(policy){
                        updateLRUState(idx, i-idx*associativity);
                    }
                    else{
                        updateSrripState(idx, i-idx*associativity, true);
                    }
                    return res;
                }
            }
        }
        this.misses++;
        // SimulatorGUI.console.append("\nIncrementing the value of misses:"+this.misses);
        return null;
    }

    public MemoryResult writeData(int addr,int updatedVal){
        this.accesses++;
        int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
        int offset=addr%blockSize;
        int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        int tagBits=tagIdx/numSets;
        MemoryResult res=null;
        for(int i=idx*associativity;i<idx*associativity+associativity;i++){
            if(tag[i]!=null){
                // System.out.println("Tag bits-------------------------- "+tagBits + " and tag["+i+"] "+tag[i]+ " and the addr is "+addr);
                if(tag[i]==tagBits){
                    cache[i*blockSize+offset]=updatedVal;
                    if(policy){
                        updateLRUState(idx, i-idx*associativity);
                    }
                    else{
                        updateSrripState(idx, i-idx*associativity, true);
                    }
                    res=new MemoryResult(this.accessLatency, cache[i*blockSize+offset]);
                    dirty_arr[i]=true;
                    return res;
                }
            }
        }
        this.misses++;
        return null;
    }
    
    // goes to main L2 cache and gets the required block from the L2 cache.
    
    public Integer[] copyFromL2(int lower_bound,int upper_bound,Cache_L2 L2Cache,Memory mem) {
    	int k=0;
    	Integer[] blockFound=new Integer[this.blockSize/4];
        boolean firstTimeDone=false;
    	for(int i=lower_bound;i<upper_bound;i+=4) {
    		MemoryResult valFromL2=L2Cache.readData(i);
    		if(valFromL2!=null) {
    			if(k<blockSize/4) {
    				blockFound[k++]=valFromL2.result;
                    if(!firstTimeDone){
                    	firstTimeDone=true;
                        latencyExtra+=valFromL2.latency;
                    }
    			}
    		}
    		else {
    			firstTimeDone=true;
    			valFromL2=L2Cache.fillCacheL2(i, mem); 
    			blockFound[k++]=valFromL2.result;
                latencyExtra+=valFromL2.latency;		
    		}
    	}
    	return blockFound;
    }
    
    // this function is called if there is cache miss in L1.
    
    public MemoryResult fillCacheL1(int addr,Cache_L2 L2Cache,Memory mem){
    	int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
    	int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        int tagBits=tagIdx/numSets;
        int offset=addr%blockSize;
        MemoryResult valFound=null;
        boolean isCacheVacant=false;
        
        for(int i=idx*associativity;i<idx*associativity+associativity;i++) {
        	if(tag[i]==null) {
        		isCacheVacant=true;
        		tag[i]=tagBits;  // updating the tag
                // System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$The tag that is getting updated is :"+i+ " to "+tagBits);
        		int lower_bound=addr-(addr%this.blockSize);
        		int upper_bound=addr+(this.blockSize-addr%this.blockSize);
        		Integer[] blockFromL2=copyFromL2(lower_bound,upper_bound,L2Cache,mem);
        		// System.out.println("----------- The block obtained from L2 cache is:");
        		// for(int k=0;k<blockFromL2.length;k++) {
        		// 	System.out.print(blockFromL2[k]+" ");
        		// }
        		for(int j=0;j<blockFromL2.length;j++) {
        			cache[i*this.blockSize+j*4]=blockFromL2[j];
        		}
        		// System.out.println("The required value is:"+cache[i*blockSize+offset]);
        		valFound=new MemoryResult(latencyExtra+this.accessLatency, cache[i*blockSize+offset]);
                // this.latencyExtra=0;
        		// System.out.println("------------ The memory result in L1 cache is returned "+valFound.result);
                if(policy){
                    updateLRUState(idx, i-idx*associativity);
                }
                else{
                    updateSrripState(idx, i-idx*associativity, true);
                }
        		break;
        	}
        }
        // System.out.println("@@@@tag[0] became "+tag[0]);
        // System.out.println("tag[1] became "+tag[1]);
        // System.out.println("tag[2] became "+tag[2]);
        // System.out.println("tag[3] became "+tag[3]);
        if(!isCacheVacant) {
        	valFound=LRU_Policy(addr,L2Cache,mem);
        }
        return valFound;
    }

    public void updateL2Cache(int lower_bound,int upper_bound,Cache_L2 L2_cache,Memory mem,int[] blockUpdated){
        for(int i=lower_bound;i<upper_bound;i+=4){
            L2_cache.writeData(i, mem, blockUpdated[(i-lower_bound)/4]);
        }
    }
    private int getEvictedBlockLru(int set){
        for(int i=0;i<this.associativity;i++){
            if(lru[set][i]==(this.associativity-1)){
                return i;
            }
        }
        System.exit(0);
        return 0;
    }
    private void updateLRUState(int set,int way){
        for(int i=0;i<this.associativity;i++){
            if(lru[set][i]<lru[set][way]){
                lru[set][i]++;
            }
            if(lru[set][i]==this.associativity){
                System.out.println("Incorrect lru value is equal to "+this.associativity);
                System.exit(0);
            }
        }
        lru[set][way]=0;
    }
    public MemoryResult LRU_Policy(int addr,Cache_L2 L2_cache, Memory mem) {
    	
        int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
    	int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        // int tagBits=tagIdx/numSets;
        // int offset=addr%blockSize;

        int blockEvicted=getEvictedBlockLru(idx);
        int addrToBeUpdated=(tag[idx*associativity+blockEvicted]*numSets+idx)*blockSize;
        updateLRUState(idx, blockEvicted); // updating the lru matrix after eviction
        int[] evictedBlock=new int[blockSize/4];
        for(int i=0;i<this.blockSize;i+=4){
            evictedBlock[i/4]=cache[i+idx*associativity*blockSize+blockEvicted*blockSize];
        }
        // System.out.println("The addr is "+addr+" and the updated addr is "+addrToBeUpdated);
        int lower_bound=addrToBeUpdated-(addrToBeUpdated%this.blockSize);
        int upper_bound=addrToBeUpdated+(this.blockSize-addrToBeUpdated%this.blockSize);
        if(dirty_arr[idx*associativity+blockEvicted]){
            updateL2Cache(lower_bound, upper_bound, L2_cache, mem, evictedBlock);
        }
        tag[idx*associativity+blockEvicted]=null;
        // System.out.println("tag[0] became "+tag[0]);
        // System.out.println("tag[1] became "+tag[1]);
        // System.out.println("tag[2] became "+tag[2]);
        // System.out.println("tag[3] became "+tag[3]);
        // System.out.println("****The address to be updated "+addrToBeUpdated);
        MemoryResult res=fillCacheL1(addr, L2_cache, mem);
        res.latency+=L2_cache.accessLatency;
        return res;
    }

    private int getEvictedBlockSrrip(int set){
        while(true){
            for(int i=0;i<this.associativity;i++){
                if(srrip[set][i]==maxRRPV){
                    return i;
                }
            }
            for(int i=0;i<this.associativity;i++){
                srrip[set][i]++;
            }
        }
    }
    private void updateSrripState(int set,int way,boolean hit){
        if(hit){
            srrip[set][way]=0;
        }
        else{
            srrip[set][way]=maxRRPV;
        }
    }
    public MemoryResult SRRIP_Policy(int addr,Cache_L2 L2_cache,Memory mem){
        int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
    	int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;

        int blockEvicted=getEvictedBlockSrrip(idx);
        int addrToBeUpdated=(tag[idx*associativity+blockEvicted]*numSets+idx)*blockSize;
        updateSrripState(idx, blockEvicted,false); // updating the lru matrix after eviction
        int[] evictedBlock=new int[blockSize/4];
        for(int i=0;i<this.blockSize;i+=4){
            evictedBlock[i/4]=cache[i+idx*associativity*blockSize+blockEvicted*blockSize];
        }
        // System.out.println("The addr is "+addr+" and the updated addr is "+addrToBeUpdated);
        int lower_bound=addrToBeUpdated-(addrToBeUpdated%this.blockSize);
        int upper_bound=addrToBeUpdated+(this.blockSize-addrToBeUpdated%this.blockSize);
        updateL2Cache(lower_bound, upper_bound, L2_cache, mem, evictedBlock);
        tag[idx*associativity+blockEvicted]=null;
        // System.out.println("tag[0] became "+tag[0]);
        // System.out.println("tag[1] became "+tag[1]);
        // System.out.println("tag[2] became "+tag[2]);
        // System.out.println("tag[3] became "+tag[3]);
        // System.out.println("****The address to be updated "+addrToBeUpdated);
        MemoryResult res=fillCacheL1(addr, L2_cache, mem);
        res.latency+=L2_cache.accessLatency;
        return res;
    }
}
