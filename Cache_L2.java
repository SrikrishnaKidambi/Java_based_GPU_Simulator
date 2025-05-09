import java.util.LinkedList;

public class Cache_L2 {
    public Integer[] tag;
    public Integer[] cache;
    public int associativity;
    public int blockSize;
    public int accessLatency;
    public int cacheSize;
    private Integer[][] lru;
    private Integer[][] srrip;
    private boolean policy; //here 0 for lru and 1 for srrip
    private int maxRRPV=3;

    public Cache_L2(int associativity,int blockSize,int cacheSize,int accessLatency,boolean whichPolicy){
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
    }
    
    // public MemoryResult LRU_Policy(int addr,Memory mem) {
    // 	// implement LRU_Policy for L1
    //     int addr_popped=lru_chain.removeLast();
    //     int tagIdx=addr_popped/blockSize; // the tagIdx variable holds the tag and index bits
    //     int offset=addr_popped%blockSize;
    //     int numTags=cacheSize/blockSize;
    //     int numSets=numTags/associativity;
    //     int idx=tagIdx%numSets;
    //     int tagPopped=tagIdx/numSets;
    //     int[] blockEvicted=new int[blockSize];
    //     for(int i=0;i<tag.length;i++){
    //         if(tag[i]==tagPopped){
    //             tag[i]=null;
    //             for(int j=0;j<blockSize;j+=4){
    //                 blockEvicted[j/4]=cache[i*blockSize+j];
    //             }
    //         }
    //     }
    //     int lower_bound=addr_popped-(addr_popped%this.blockSize);
    //     int upper_bound=addr_popped+(this.blockSize-addr_popped%this.blockSize);
    //     updateMem(lower_bound, upper_bound,mem, blockEvicted);
    //     MemoryResult valFound=fillCacheL2(addr, mem);
    //     return valFound;
    // }
    
    public void updateMem(int lower_bound,int upper_bound,Memory mem,int[] blockEvicted){
        for(int i=lower_bound;i<upper_bound;i+=4){
            mem.memory[i]=blockEvicted[(i-lower_bound)/4];
        }
    }

    public MemoryResult readData(int addr){
        int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
        int offset=addr%blockSize;
        int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        int tagBits=tagIdx/numSets;
        for(int i=idx*associativity;i<idx*associativity+associativity;i++){
            if(tag[i]!=null) {
            	if(tag[i]==tagBits){
                    if(policy){
                        updateLRUState(idx, i-idx*associativity);
                    }
                    else{
                        updateSrripState(idx, i-idx*associativity, true);
                    }
                    return new MemoryResult(this.accessLatency, cache[i*blockSize+offset]);
                }
            }
        }
        return null;
    }
    
    public void writeData(int addr,Memory mem,int eleUpdated){
        // int lower_bound=addr-(addr%this.blockSize);
        // int upper_bound=addr+(this.blockSize-(addr%this.blockSize));
        int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
    	int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        int tagBits=tagIdx/numSets;
        int offset=addr%blockSize;
        for(int i=idx*associativity;i<idx*associativity+associativity;i++){
            if(tag[i]!=null) {
            	if(tag[i]==tagBits){
                    cache[i*blockSize+offset]=eleUpdated;
                    if(policy){
                        updateLRUState(idx, i-idx*associativity);
                    }
                    else{
                        updateSrripState(idx, i-idx*associativity, true);
                    }
                    return;
                }
            }
        }
        fillCacheL2(addr, mem);
    }
    public MemoryResult fillCacheL2(int addr,Memory mem){
    	System.out.println("-------------------- Called fillCacheL2 function");
    	int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
    	int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        int tagBits=tagIdx/numSets;
        int offset=addr%blockSize;
        Integer valFound=null;
        boolean isCacheVacant=false;
        
        for(int i=idx*associativity;i<idx*associativity+associativity;i++) {   // traverse through the tag array based on the associativity (select the set)
        	if(tag[i]==null) {
        		int lower_bound=addr-(addr%this.blockSize);
                int upper_bound=addr+(this.blockSize-(addr%this.blockSize));
                tag[i]=tagBits;
                for(int j=lower_bound;j<upper_bound;j+=4) { 
                	cache[i*blockSize+j-lower_bound]=mem.memory[j];    
                }
                valFound=cache[i*blockSize+offset];
                System.out.println("--------------The value found by after a miss in L2 cache :"+valFound);
                isCacheVacant=true;
                if(policy){
                    updateLRUState(idx, i-idx*associativity);
                }
                else{
                    updateSrripState(idx, i-idx*associativity, true);
                }
                break;
        	}
        }
        if(!isCacheVacant) {
        	MemoryResult res=LRU_Policy(addr,mem);
            return res;
        }
        System.out.println("The memory result is returned ");
        return new MemoryResult(this.accessLatency+mem.memoryAccessLatency, valFound);
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
            System.out.println("Saiman thopu"+way);
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
    public MemoryResult LRU_Policy(int addr, Memory mem) {
    	// implement LRU_Policy for L1
        int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
    	int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        // int tagBits=tagIdx/numSets;
        // int offset=addr%blockSize;

        int blockEvicted=getEvictedBlockLru(idx);
        int addrToBeUpdated=(tag[idx*associativity+blockEvicted]*numSets+idx)*blockSize;
        System.out.println("The block evicted is "+blockEvicted);
        updateLRUState(idx, blockEvicted); // updating the lru matrix after eviction
        int lower_bound=addrToBeUpdated-(addrToBeUpdated%this.blockSize);
        int upper_bound=addrToBeUpdated+(this.blockSize-(addrToBeUpdated%this.blockSize));
        int[] evictedBlock = new int[blockSize/4];
        for(int i=0;i<this.blockSize;i+=4){
            evictedBlock[i/4]=cache[i+idx*associativity*blockSize+blockEvicted*blockSize];
        }
        updateMem(lower_bound, upper_bound, mem, evictedBlock);
        tag[idx*associativity+blockEvicted]=null;
        MemoryResult res=fillCacheL2(addr, mem);
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
        // implement LRU_Policy for L1
        int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
    	int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        // int tagBits=tagIdx/numSets;
        // int offset=addr%blockSize;

        int blockEvicted=getEvictedBlockSrrip(idx);
        int addrToBeUpdated=(tag[idx*associativity+blockEvicted]*numSets+idx)*blockSize;
        System.out.println("The block evicted is "+blockEvicted);
        updateSrripState(idx, blockEvicted,false); // updating the lru matrix after eviction
        int lower_bound=addrToBeUpdated-(addrToBeUpdated%this.blockSize);
        int upper_bound=addrToBeUpdated+(this.blockSize-(addrToBeUpdated%this.blockSize));
        int[] evictedBlock = new int[blockSize/4];
        for(int i=0;i<this.blockSize;i+=4){
            evictedBlock[i/4]=cache[i+idx*associativity*blockSize+blockEvicted*blockSize];
        }
        updateMem(lower_bound, upper_bound, mem, evictedBlock);
        tag[idx*associativity+blockEvicted]=null;
        MemoryResult res=fillCacheL2(addr, mem);
        return res;
    }
}