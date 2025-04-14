public class Cache_L1D {
    public Integer[] tag;
    public Integer[] cache;
    public int associativity;
    public int blockSize;
    public int accessLatency;
    public int cacheSize;

    public Cache_L1D(int associativity,int blockSize,int cacheSize,int accessLatency){
        this.associativity=associativity;
        this.blockSize=blockSize;
        this.cacheSize=cacheSize;
        this.accessLatency=accessLatency;

        cache=new Integer[cacheSize];
        tag=new Integer[cacheSize/blockSize];
    }

    public Integer readData(int addr){
        int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
        int offset=addr%blockSize;
        int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        int tagBits=tagIdx/numSets;
        for(int i=idx*associativity;i<idx*associativity+associativity;i++){
            if(tag[i]==tagBits){
                return cache[i*blockSize+offset];
            }
        }
        return null;
    }
    
    // goes to main L2 cache and gets the required block from the L2 cache.
    
    public Integer[] copyFromL2(int lower_bound,int upper_bound,Cache_L2 L2Cache,Memory mem) {
    	int k=0;
    	Integer[] blockFound=new Integer[this.blockSize/4];
    	for(int i=lower_bound;i<upper_bound;i+=4) {
    		Integer valFromL2=L2Cache.readData(i);
    		if(valFromL2!=null) {
    			if(k<blockSize/4) {
    				blockFound[k++]=valFromL2;
    			}
    		}
    		else {
    			valFromL2=L2Cache.fillCacheL2(i, mem);   
    			blockFound[k++]=valFromL2;		
    		}
    	}
    	return blockFound;
    }
    
    public void LRU_Policy() {
    	// implement LRU_Policy for L1
    }
    
    // this function is called if there is cache miss in L1.
    
    public Integer fillCacheL1(int addr,Cache_L2 L2Cache,Memory mem){
   
    	int tagIdx=addr/blockSize; // the tagIdx variable holds the tag and index bits
    	int numTags=cacheSize/blockSize;
        int numSets=numTags/associativity;
        int idx=tagIdx%numSets;
        int tagBits=tagIdx/numSets;
        int offset=addr%blockSize;
        Integer valFound=null;
        boolean isCacheVacant=false;
        
        for(int i=idx*associativity;i<idx*associativity+associativity;i++) {
        	if(tag[i]==null) {
        		isCacheVacant=true;
        		tag[i]=tagBits;  // updating the tag
        		int lower_bound=addr-(addr%this.blockSize);
        		int upper_bound=addr+(this.blockSize-addr%this.blockSize);
        		Integer[] blockFromL2=copyFromL2(lower_bound,upper_bound,L2Cache,mem);
        		for(int j=0;j<blockFromL2.length;j++) {
        			cache[i*this.blockSize+j*4]=blockFromL2[j];
        		}
        		valFound=cache[i*blockSize+offset];
        		break;
        	}
        }
        if(!isCacheVacant) {
        	LRU_Policy();
        }
        return valFound;
    }
}
