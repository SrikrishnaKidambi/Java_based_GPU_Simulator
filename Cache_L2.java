public class Cache_L2 {

    public Cache_L2(int associativity,int blockSize,int cacheSize,int accessLatency){
        this.associativity=associativity;
        this.blockSize=blockSize;
        this.cacheSize=cacheSize;
        this.accessLatency=accessLatency;

        cache=new Integer[cacheSize];
        tag=new Integer[cacheSize/blockSize];
    }
    
    public void LRU_Policy() {
    	// function yet to be implemented with all the parameters to be passed and logic also.
    }
    
    // this method should actually return an instance of MemoryResult Class that has the value and latency required to get the value.

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

    // this method should actually return an instance of MemoryResult Class that has the value and latency required to get the value.
    
    public Integer fillCacheL2(int addr,Memory mem){
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
                isCacheVacant=true;
                break;
        	}
        }
        if(!isCacheVacant) {
        	LRU_Policy();
        }
        return valFound;
    }
    
    public Integer[] tag;
    public Integer[] cache;
    public int associativity;
    public int blockSize;
    public int accessLatency;
    public int cacheSize;
}