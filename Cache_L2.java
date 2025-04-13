public class Cache_L2 {
    public Integer[] tag;
    public Integer[] cache;
    public int associativity;
    public int blockSize;
    public int accessLatency;
    public int cacheSize;

    public Cache_L2(int associativity,int blockSize,int cacheSize,int accessLatency){
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

    public Integer fillCacheL2(int addr,Memory memory){
        
    }
}
