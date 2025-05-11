public class CacheConfig {
    public int CL1_latency=1;
    public int CL1_associativitity=1;
    public int CL1_blockSize=8;
    public int CL1_cacheSize=128;
    public int CL2_latency=3;
    public int CL2_associativity=1;
    public int CL2_blockSize=16;
    public int CL2_cacheSize=512;
    public int mem_latency=5;
    public boolean CL1_policy=true; // false indicates to use srrip and true indicates to use lru
    public boolean CL2_policy=true;
    public CacheConfig(String textFile){

    }
}
