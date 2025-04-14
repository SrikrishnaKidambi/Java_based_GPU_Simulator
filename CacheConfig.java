public class CacheConfig {
    public int CL1_latency=10;
    public int CL1_associativitity=2;
    public int CL1_blockSize=32;
    public int CL1_cacheSize=256;
    public int CL2_latency=25;
    public int CL2_associativity=16;
    public int CL2_blockSize=64;
    public int CL2_cacheSize=1024;
    public int mem_latency=100;

    public CacheConfig(String textFile){

    }
}
